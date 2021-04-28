package ru.stm.delete_rows.service.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stm.delete_rows.aspect.annotation.LogExecutionTime;
import ru.stm.delete_rows.service.DatabaseService;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.TRUNCATE_TABLE_BY_NAME;

/**
 * Стратегия полной очистки , если д.б. удалены все записи
 */
@Service
@Slf4j
public class TruncateRemoveStrategy extends ARemoveStrategy implements RemoveStrategy {
    private static final double RECOMMENDED_PERCENT = 100;


    public TruncateRemoveStrategy(DatabaseService databaseService) {
        super(databaseService);
    }

    @Override
    @Transactional
    @LogExecutionTime
    public void remove(String table, String date) {
        log.info("Удаление всех записей");
        databaseService.execute(format(TRUNCATE_TABLE_BY_NAME,table));
        log.info("Удаление записей завершено");
    }

    @Override
    public boolean isRecommended(double percent) {
        return percent == RECOMMENDED_PERCENT;
    }
}
