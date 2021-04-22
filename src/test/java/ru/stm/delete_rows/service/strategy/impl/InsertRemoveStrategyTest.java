package ru.stm.delete_rows.service.strategy.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsertRemoveStrategyTest {

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    @Test
    void remove() {
        InsertRemoveStrategy strategy = new InsertRemoveStrategy(jdbcTemplate);
        strategy.remove("test_table","2000-12-31 01:02:03",0);
        verify(jdbcTemplate, times(1))
                .execute("create table temp_table as select * from test_table where ddate > '2000-12-31 01:02:03'");
        verify(jdbcTemplate, times(1))
                .execute("drop table test_table");
        verify(jdbcTemplate, times(1))
                .execute("alter table temp_table rename to test_table");
    }
}