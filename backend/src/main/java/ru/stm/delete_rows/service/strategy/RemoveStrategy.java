package ru.stm.delete_rows.service.strategy;

/**
 * Стратегия удаления
 */
public interface RemoveStrategy {
    /**
     * Метод удаляет данные из таблицы
     * @param table имя таблицы
     * @param date дата старше которой будут удалены данные
     */
    void remove(String table, String date);

    /**
     * Сопоставляет процент удаляемых записей из таблицы с рекомендуемым процентом для выбранной стратегии
     * @param percent процент удаляемых записей
     * @return true при соответсвии стратегии параметрам, иначе false
     */
    boolean isRecommended(long percent);
}
