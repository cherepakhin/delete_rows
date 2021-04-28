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
        String date = requestDto.getDate();
        int countOfRecordsToRemove = getCountOfRecordsToRemove(tableName, date);
        context.setStrategy(getStrategy(tableName, countOfRecordsToRemove));
        context.execute(tableName, date);
        watch.stop();
        return new ResponseDto()
                .setTime(new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(watch.getLastTaskTimeMillis())))
                .setRowsDeleted(countOfRecordsToRemove);
    }

    /**
     * Выбирает статегию удаления на основании вычислений процента удаляемых записей
     * @param table имя таблицы
     * @param removeRowsCount  исло записей для удаления
     * @return стратегию удаления
     */
    private RemoveStrategy getStrategy(String table, Integer removeRowsCount) {
        int percent = calculatePercentage(removeRowsCount, getCountOfRecords(table));
        return removeStrategyList.stream()
                .filter(removeStrategy -> removeStrategy.isRecommended(percent))
                .findFirst()
                .orElseThrow(() -> new RemoveStrategyNotFoundException("Remove strategy not found for this operation"));
    }

    /**
     * Считает общее число записей в таблице
     * @param table имя таблицы
     * @return общее число записей в таблице
     */
    private Integer getCountOfRecords(String table) {
        return databaseService.queryForObject(
                format(SELECT_COUNT_OF_RECORD_BY_TABLE_NAME, table),
                Integer.class);
    }

    /**
     * Вычисляет кол-во записей которые должны быть удалены
     * @param table имя таблицы
     * @param date дата старше которой будут удалены данные
     * @return число записей для удаления
     */
    private Integer getCountOfRecordsToRemove(String table, String date) {
        return databaseService.queryForObject(
                format(SELECT_COUNT_OF_RECORDS_BY_DATE, table, date),
                Integer.class);
    }

    /**
     * Вычисляет количество записей которые должны быть удалены в процентном соотношении
     * @param obtained число записей для удаления
     * @param total общее число записей в таблице
     * @return
     */
    private int calculatePercentage(Integer obtained, Integer total) {
        return obtained * MAX_PERCENT / total;
    }
}
