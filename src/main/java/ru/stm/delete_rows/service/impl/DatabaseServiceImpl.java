package ru.stm.delete_rows.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.stm.delete_rows.service.DatabaseService;

import javax.sql.DataSource;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public DatabaseServiceImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void execute(String sqlQuery) {
        jdbcTemplate.execute(sqlQuery);
    }

    @Override
    public <T> T queryForObject(String sqlQuery, Class<? extends T> className) {
        return jdbcTemplate.queryForObject(sqlQuery, className);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
