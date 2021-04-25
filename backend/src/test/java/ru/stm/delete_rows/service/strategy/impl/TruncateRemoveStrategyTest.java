package ru.stm.delete_rows.service.strategy.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.stm.delete_rows.service.DatabaseService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TruncateRemoveStrategyTest {

    DatabaseService databaseService = mock(DatabaseService.class);

    @Test
    void remove() {
        TruncateRemoveStrategy strategy = new TruncateRemoveStrategy(databaseService);
        strategy.remove("test_table", "fromDate");
        verify(databaseService, times(1)).execute("truncate test_table");
    }
}