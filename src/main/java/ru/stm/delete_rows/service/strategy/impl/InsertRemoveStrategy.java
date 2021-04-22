package ru.stm.delete_rows.service.strategy.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.*;

/**
 * Стратегия удаления с использованием временной таблицы
 * <pre>
 *     1.
 * </pre>
 */
public class InsertRemoveStrategy extends ARemoveStrategy implements RemoveStrategy {

    public InsertRemoveStrategy(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public void remove(String table, String date, int portion) {
        jdbcTemplate.execute(format(CREATE_TEMP_TABLE_BY_SELECT, table, date));
        jdbcTemplate.execute(format(DROP_TABLE_BY_NAME, table));
        jdbcTemplate.execute(format(RENAME_TEMP_TABLE, table));
    }
}
