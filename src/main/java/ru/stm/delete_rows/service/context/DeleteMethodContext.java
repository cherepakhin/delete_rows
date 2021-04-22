package ru.stm.delete_rows.service.context;

import ru.stm.delete_rows.service.strategy.RemoveStrategy;

public interface DeleteMethodContext {
    void execute(String table, String date, int portion);
    void setStrategy(RemoveStrategy removeStrategy);
}
