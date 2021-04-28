package ru.stm.delete_rows.service;

import javax.sql.DataSource;

/**
 * Сервис взаимодействия с базой данных
 */
public interface DatabaseService {
    /**
     * Выполняет запрос к базе данных
     * @param sqlQuery строка запроса
     */
    void execute(String sqlQuery);

    /**
     * Выполняет запрос к базе данных. Приводит возвращаемое значение к указанному типу
     * @param sqlQuery строка запроса
     * @param className имя класса
     * @param <T> возвращаемое значение
     * @return возвращает результат запроса конвертирую в заданный тип обьекта
     */
    <T> T queryForObject(String sqlQuery, Class<? extends T> className);

    /**
     * Возвращает бин dataSource;
     * @return dataSource
     */
    DataSource getDataSource();
}
