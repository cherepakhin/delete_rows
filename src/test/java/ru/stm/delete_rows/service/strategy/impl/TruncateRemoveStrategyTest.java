package ru.stm.delete_rows.service.strategy.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TruncateRemoveStrategyTest {

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    @Test
    void remove() {
        TruncateRemoveStrategy strategy = new TruncateRemoveStrategy(jdbcTemplate);
        strategy.remove("test_table", "fromDate", 0);
        verify(jdbcTemplate, times(1)).execute("truncate test_table");
    }
}