package ru.stm.delete_rows.service.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.DELETE_DATA_BY_SELECT;
import static ru.stm.delete_rows.constants.Queries.SELECT_COUNT_OF_RECORDS_BY_DATE;

/**
 * Удаление порциями через
 * <pre>
 *  delete from table where id in
 *      (select id from table where ddate<:fromDate limit :portion)
 * </pre>
 */
@Slf4j
public class PartitionRemoveStrategy extends ARemoveStrategy implements RemoveStrategy {

    public PartitionRemoveStrategy(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void remove(String table, String date, int portion) {
        Integer count = jdbcTemplate.queryForObject(
                format(SELECT_COUNT_OF_RECORDS_BY_DATE, table, date),
                Integer.class);
        if (count == null || count == 0) {
            log.info("В таблице {} нечего удалять", table);
            return;
        }
        int countCicle = count / portion + 1;
        log.info("В таблице {} записей для удаления {}", table, count);
        String sql = format(
                DELETE_DATA_BY_SELECT,
                table, table, date, portion);
        for (int i = 0; i < countCicle; i++) {
            jdbcTemplate.execute(sql);
            log.info("Таблица: {}. Удалено {} из {} ", table, i * portion, count);
        }
        log.info("Закончили упражнение.");
    }
}
