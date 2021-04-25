package ru.stm.delete_rows.service.context.impl;

import lombok.extern.slf4j.Slf4j;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

@Slf4j
public class DeleteMethodContextImpl implements DeleteMethodContext {
    private RemoveStrategy removeStrategy;

    @Override
    public void execute(String table, String date) {
        removeStrategy.remove(table, date);
    }

    @Override
    public void setStrategy(RemoveStrategy removeStrategy) {
        this.removeStrategy = removeStrategy;
    }
}
