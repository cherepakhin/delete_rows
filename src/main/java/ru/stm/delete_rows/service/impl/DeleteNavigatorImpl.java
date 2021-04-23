package ru.stm.delete_rows.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;
import ru.stm.delete_rows.exception.RemoveStrategyNotFoundException;
import ru.stm.delete_rows.service.DatabaseService;
import ru.stm.delete_rows.service.DeleteNavigator;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.SELECT_COUNT_OF_RECORDS_BY_DATE;
import static ru.stm.delete_rows.constants.Queries.SELECT_COUNT_OF_RECORD_BY_TABLE_NAME;

@Service
@Slf4j
public class DeleteNavigatorImpl implements DeleteNavigator {
    private static final int MAX_PERCENT = 100;
    private final DeleteMethodContext context;
    private final DatabaseService databaseService;
    private final List<RemoveStrategy> removeStrategyList;

    public DeleteNavigatorImpl(DeleteMethodContext context,
                               DatabaseService databaseService,
                               List<RemoveStrategy> removeStrategyList) {
        this.databaseService = databaseService;
        this.context = context;
        this.removeStrategyList = removeStrategyList;
    }

    @Override
    public ResponseDto deleteRowsByDate(RequestDto requestDto) {
        StopWatch watch = new StopWatch();
        watch.start();
        String tableName = requestDto.getTableName();
        String date = String.valueOf(requestDto.getDate());
        int countOfRecordsToRemove = getCountOfRecordsToRemove(tableName, date);
        context.setStrategy(getStrategy(tableName, countOfRecordsToRemove));
        context.execute(tableName, date, requestDto.getPortion());
        watch.stop();
        return new ResponseDto()
                .setTime(new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(watch.getLastTaskTimeMillis())))
                .setRowsDeleted(countOfRecordsToRemove);
    }

    private RemoveStrategy getStrategy(String table, Integer removeRowsCount) {
        int percent = calculatePercentage(removeRowsCount, getCountOfRecords(table));
        return removeStrategyList.stream()
                .filter(removeStrategy -> removeStrategy.isRecommended(percent))
                .findFirst()
                .orElseThrow(() -> new RemoveStrategyNotFoundException("Remove strategy not found for this operation"));
    }

    private Integer getCountOfRecords(String table) {
        return databaseService.queryForObject(
                format(SELECT_COUNT_OF_RECORD_BY_TABLE_NAME, table),
                Integer.class);
    }

    private Integer getCountOfRecordsToRemove(String table, String date) {
        return databaseService.queryForObject(
                format(SELECT_COUNT_OF_RECORDS_BY_DATE, table, date),
                Integer.class);
    }

    private int calculatePercentage(Integer obtained, Integer total) {
        return obtained * MAX_PERCENT / total;
    }
}
