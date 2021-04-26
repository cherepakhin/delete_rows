package ru.stm.delete_rows.service.strategy.impl;

import org.junit.jupiter.api.Test;
import ru.stm.delete_rows.service.DatabaseService;

import static org.mockito.Mockito.*;

public class PartitionRemoveStrategyTest {

    DatabaseService databaseService = mock(DatabaseService.class);

    @Test
    void remove() {
        PartitionRemoveStrategy strategy = new PartitionRemoveStrategy(databaseService);
        strategy.remove("test_table","2000-12-31");
        verify(databaseService, times(1))
                .execute("select count(*) from test_table where ddate < '2000-12-31'");
        verify(databaseService, times(1))
                .execute("delete from test_table where id in (select id from test_table where ddate < '2000-12-31' limit 10)");


    }
}
