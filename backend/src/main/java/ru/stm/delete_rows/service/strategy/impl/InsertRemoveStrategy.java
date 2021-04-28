package ru.stm.delete_rows.service.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stm.delete_rows.aspect.annotation.LogExecutionTime;
import ru.stm.delete_rows.service.DatabaseService;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.*;

/**
 * Стратегия удаления с использованием временной таблицы
 * <pre>
 *     1.
 * </pre>
 */
@Service
@Slf4j
public class InsertRemoveStrategy extends ARemoveStrategy implements RemoveStrategy {
    private static final double RECOMMENDED_PERCENT = 30;
    private static final double MAX_PERCENT = 100;

    public InsertRemoveStrategy(DatabaseService databaseService) {
        super(databaseService);
    }


    @Override
    @Transactional
    @LogExecutionTime
    public void remove(String table, String date) {
        log.info("Удаление методом вставки");
        databaseService.execute(format(CREATE_TEMP_TABLE_BY_SELECT, table, date));
        databaseService.execute(format(DROP_TABLE_BY_NAME, table));
        databaseService.execute(format(RENAME_TEMP_TABLE, table));
        log.info("Удаление завершено");
    }

    @Override
    public boolean isRecommended(double percent) {
        return percent >= RECOMMENDED_PERCENT && percent != MAX_PERCENT;
    }
}
