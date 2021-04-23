package ru.stm.delete_rows.service;

import javax.sql.DataSource;

public interface DatabaseService {
    void execute(String sqlQuery);
    <T> T queryForObject(String sqlQuery, Class<? extends T> className);
    DataSource getDataSource();
}
