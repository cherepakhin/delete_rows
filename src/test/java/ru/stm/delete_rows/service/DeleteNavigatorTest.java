package ru.stm.delete_rows.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.impl.DeleteNavigatorImpl;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.InsertRemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.PartitionRemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.TruncateRemoveStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DeleteNavigatorTest {
    DeleteMethodContext context = spy(DeleteMethodContext.class);
    DatabaseService databaseService = spy(DatabaseService.class);
    InsertRemoveStrategy insertRemoveStrategy = spy(new InsertRemoveStrategy(databaseService));
    PartitionRemoveStrategy partitionRemoveStrategy= spy(new PartitionRemoveStrategy(databaseService));
    TruncateRemoveStrategy truncateRemoveStrategy= spy(new TruncateRemoveStrategy(databaseService));
    List<RemoveStrategy> removeStrategyList;
    RequestDto requestDto;

    @BeforeEach
    public void setUp() {
        requestDto = new RequestDto();
        requestDto.setTableName("testTable");
        requestDto.setDate("2021-04-22");
    }

    @AfterEach
    public void tearDown() {
        requestDto = null;
    }

    @Test
    public void deleteRowsByDateTest() throws Exception {
        removeStrategyList = spy(new ArrayList<>());
        removeStrategyList.add(insertRemoveStrategy);
        removeStrategyList.add(partitionRemoveStrategy);
        removeStrategyList.add(truncateRemoveStrategy);
        DeleteNavigator deleteNavigator = Mockito.spy(new DeleteNavigatorImpl(context, databaseService, removeStrategyList));
        doReturn(false).when(insertRemoveStrategy).isRecommended(anyInt());
        doReturn(true).when(partitionRemoveStrategy).isRecommended(anyInt());
        doReturn(false).when(truncateRemoveStrategy).isRecommended(anyInt());
        when(databaseService.queryForObject("select count(*) from testTable where ddate < '2021-04-22'", Integer.class)).thenReturn(10);
        when(databaseService.queryForObject("select count(*) from testTable", Integer.class)).thenReturn(100);
        doNothing().when(partitionRemoveStrategy).remove(anyString(), anyString(), anyInt());
        doAnswer(invocation -> partitionRemoveStrategy).when(context).setStrategy(any());
        deleteNavigator.deleteRowsByDate(requestDto);
        verify(partitionRemoveStrategy, times(1)).isRecommended(10);
       // verify(partitionRemoveStrategy, times(1)).remove(anyString(), anyString(), anyInt());
    }
}
