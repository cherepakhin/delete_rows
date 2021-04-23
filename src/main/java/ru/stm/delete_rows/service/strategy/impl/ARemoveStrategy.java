package ru.stm.delete_rows.service.strategy.impl;

import ru.stm.delete_rows.service.DatabaseService;

public abstract class ARemoveStrategy {
    protected DatabaseService databaseService;

    protected ARemoveStrategy(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
}
