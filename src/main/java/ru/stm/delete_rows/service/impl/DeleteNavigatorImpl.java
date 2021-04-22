package ru.stm.delete_rows.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.service.DeleteNavigator;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.InsertRemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.PartitionRemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.TruncateRemoveStrategy;

import javax.sql.DataSource;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.SELECT_COUNT_OF_RECORDS_BY_DATE;
import static ru.stm.delete_rows.constants.Queries.SELECT_COUNT_OF_RECORD_BY_TABLE_NAME;

public class DeleteNavigatorImpl implements DeleteNavigator {
    private final int PERCENT_100 = 100;
    private final int PERCENT_60 = 60;
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final DeleteMethodContext context;

    public DeleteNavigatorImpl(DataSource dataSource, DeleteMethodContext context) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.context = context;
    }

    @Override
    public void deleteRowsByDate(RequestDto requestDto) {
        String tableName = requestDto.getTableName();
        String date = requestDto.getDate();
        context.setStrategy(getStrategy(tableName, date));
        context.execute(tableName, date, requestDto.getPortion());
    }

    private RemoveStrategy getStrategy(String table, String date) {
        int percent = calculatePercentage(getCountOfRecordsToRemove(table, date), getCountOfRecords(table));
        if (percent == PERCENT_100) {
            return new TruncateRemoveStrategy();
        }
        if (percent > PERCENT_60) {
            return new InsertRemoveStrategy();
        }
        return new PartitionRemoveStrategy(jdbcTemplate);
    }

    private Integer getCountOfRecords(String table) {
        return jdbcTemplate.queryForObject(
                format(SELECT_COUNT_OF_RECORD_BY_TABLE_NAME, table),
                Integer.class);
    }

    private Integer getCountOfRecordsToRemove(String table, String date) {
        return jdbcTemplate.queryForObject(
                format(SELECT_COUNT_OF_RECORDS_BY_DATE, table, date),
                Integer.class);
    }

    private int calculatePercentage(int obtained, int total) {
        return obtained * 100 / total;
    }
}
