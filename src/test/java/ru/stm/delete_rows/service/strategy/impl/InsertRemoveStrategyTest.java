package ru.stm.delete_rows.service.strategy.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.stm.delete_rows.service.DatabaseService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsertRemoveStrategyTest {

    DatabaseService databaseService = mock(DatabaseService.class);
    @Test
    void remove() {
        InsertRemoveStrategy strategy = new InsertRemoveStrategy(databaseService);
        strategy.remove("test_table","2000-12-31 01:02:03",0);
        verify(databaseService, times(1))
                .execute("create table temp_table as select * from test_table where ddate > '2000-12-31 01:02:03'");
        verify(databaseService, times(1))
                .execute("drop table test_table");
        verify(databaseService, times(1))
                .execute("alter table temp_table rename to test_table");
    }
}