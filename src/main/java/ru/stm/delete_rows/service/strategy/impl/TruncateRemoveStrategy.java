package ru.stm.delete_rows.service.strategy.impl;

import ru.stm.delete_rows.service.strategy.RemoveStrategy;

public class TruncateRemoveStrategy implements RemoveStrategy {
    @Override
    public void remove(String table, String date, int portion) {
        //todo clear table by truncate operator
    }
}
