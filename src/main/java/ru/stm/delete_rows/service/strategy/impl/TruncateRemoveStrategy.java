package ru.stm.delete_rows.service.strategy.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.TRUNCATE_TABLE_BY_NAME;

/**
 * Стратегия полной очистки , если д.б. удалены все записи
 */
public class TruncateRemoveStrategy extends ARemoveStrategy implements RemoveStrategy {

    public TruncateRemoveStrategy(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void remove(String table, String date, int portion) {
        jdbcTemplate.execute(format(TRUNCATE_TABLE_BY_NAME,table));
    }
}
