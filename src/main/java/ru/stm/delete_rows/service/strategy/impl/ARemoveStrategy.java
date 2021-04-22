package ru.stm.delete_rows.service.strategy.impl;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ARemoveStrategy {
    protected final JdbcTemplate jdbcTemplate;

    protected ARemoveStrategy(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
