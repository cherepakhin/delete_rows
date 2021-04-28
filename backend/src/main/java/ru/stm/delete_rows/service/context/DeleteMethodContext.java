package ru.stm.delete_rows.service.context;

import ru.stm.delete_rows.service.strategy.RemoveStrategy;

/**
 * Контекст статегии удаления
 */
public interface DeleteMethodContext {
    /**
     * Выполняет вызов метода удаления установленной стратегии
     * @param table имя таблицы
     * @param date дата старше которой будут удалены данные
     */
    void execute(String table, String date);

    /**
     * Устанавливает выбранную стратегию в контекст
     * @param removeStrategy стратегия удаления записей
     */
    void setStrategy(RemoveStrategy removeStrategy);
    RemoveStrategy getStrategy();
}
