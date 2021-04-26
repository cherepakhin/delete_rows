package ru.stm.delete_rows.service.context.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.stm.delete_rows.service.DatabaseService;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.PartitionRemoveStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteMethodContextTest {

    DeleteMethodContext context;
    DatabaseService databaseService = spy(DatabaseService.class);

    @BeforeEach
    public void setUp() {
        context = new DeleteMethodContextImpl();
    }

    @AfterEach
    public void tearDown() {
        context = null;
    }

    @Test
    public void executeAndGetStrategyTest() {
        PartitionRemoveStrategy removeStrategy = spy(new PartitionRemoveStrategy(databaseService));
        doNothing().when(removeStrategy).remove(anyString(), anyString());
        context.setStrategy(removeStrategy);
        context.execute(anyString(), anyString());

        assertEquals(removeStrategy, context.getStrategy());
        verify(removeStrategy, times(1)).remove(anyString(), anyString());
    }
}
