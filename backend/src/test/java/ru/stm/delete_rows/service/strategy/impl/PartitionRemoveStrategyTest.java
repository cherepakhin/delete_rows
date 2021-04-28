package ru.stm.delete_rows.service.strategy.impl;

import org.junit.jupiter.api.Test;
import ru.stm.delete_rows.service.DatabaseService;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class PartitionRemoveStrategyTest {

    DatabaseService databaseService = spy(DatabaseService.class);

    @Test
    void remove() throws InterruptedException {
        when(databaseService.queryForObject(anyString(), any())).thenReturn(100);
        PartitionRemoveStrategy strategy = new PartitionRemoveStrategy(databaseService);
        strategy.remove("test_table","2000-12-31");
        TimeUnit.SECONDS.sleep(2);
        verify(databaseService, times(1))
                .queryForObject("select count(*) from test_table where ddate < '2000-12-31'", Integer.class);
        verify(databaseService, times(10))
                .execute("delete from test_table where id in (select id from test_table where ddate < '2000-12-31' limit 10)");


    }
}
