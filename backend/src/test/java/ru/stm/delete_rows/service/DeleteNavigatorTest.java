package ru.stm.delete_rows.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.exception.RemoveStrategyNotFoundException;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.context.impl.DeleteMethodContextImpl;
import ru.stm.delete_rows.service.impl.DeleteNavigatorImpl;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.InsertRemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.PartitionRemoveStrategy;
import ru.stm.delete_rows.service.strategy.impl.TruncateRemoveStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteNavigatorTest {
    DeleteMethodContext context = spy(new DeleteMethodContextImpl());
    DatabaseService databaseService = spy(DatabaseService.class);
    InsertRemoveStrategy insertRemoveStrategy = spy(new InsertRemoveStrategy(databaseService));
    PartitionRemoveStrategy partitionRemoveStrategy= spy(new PartitionRemoveStrategy(databaseService));
    TruncateRemoveStrategy truncateRemoveStrategy= spy(new TruncateRemoveStrategy(databaseService));
    List<RemoveStrategy> removeStrategyList;
    RequestDto requestDto;
    DeleteNavigator deleteNavigator;

    @BeforeEach
    public void setUp() {
        requestDto = new RequestDto();
        requestDto.setTableName("testTable");
        requestDto.setDate("2021-04-22");

        removeStrategyList = spy(new ArrayList<>());
        removeStrategyList.add(insertRemoveStrategy);
        removeStrategyList.add(partitionRemoveStrategy);
        removeStrategyList.add(truncateRemoveStrategy);

        deleteNavigator = Mockito.spy(new DeleteNavigatorImpl(context, databaseService, removeStrategyList));
    }

    @AfterEach
    public void tearDown() {
        requestDto = null;
    }

    @Test
    public void deleteRowsByDateByPartitionRemoveStrategyTest() {
        doReturn(false).when(truncateRemoveStrategy).isRecommended(10);
        doReturn(false).when(insertRemoveStrategy).isRecommended(10);
        doReturn(true).when(partitionRemoveStrategy).isRecommended(10);
        when(databaseService.queryForObject("select count(*) from testTable where ddate < '2021-04-22'", Integer.class)).thenReturn(10);
        when(databaseService.queryForObject("select count(*) from testTable", Integer.class)).thenReturn(100);
        doNothing().when(partitionRemoveStrategy).remove(anyString(), anyString());
        deleteNavigator.deleteRowsByDate(requestDto);
        verify(partitionRemoveStrategy, times(1)).isRecommended(10);
        verify(partitionRemoveStrategy, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void deleteRowsByDateByInsertRemoveStrategyTest() {
        doReturn(false).when(truncateRemoveStrategy).isRecommended(60);
        doReturn(true).when(insertRemoveStrategy).isRecommended(60);
        doReturn(false).when(partitionRemoveStrategy).isRecommended(60);
        when(databaseService.queryForObject("select count(*) from testTable where ddate < '2021-04-22'", Integer.class)).thenReturn(60);
        when(databaseService.queryForObject("select count(*) from testTable", Integer.class)).thenReturn(100);
        doNothing().when(insertRemoveStrategy).remove(anyString(), anyString());
        deleteNavigator.deleteRowsByDate(requestDto);
        verify(insertRemoveStrategy, times(1)).isRecommended(60);
        verify(insertRemoveStrategy, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void deleteRowsByDateByTruncateRemoveStrategyTest() {
        doReturn(true).when(truncateRemoveStrategy).isRecommended(100);
        doReturn(false).when(insertRemoveStrategy).isRecommended(100);
        doReturn(false).when(partitionRemoveStrategy).isRecommended(100);
        when(databaseService.queryForObject("select count(*) from testTable where ddate < '2021-04-22'", Integer.class)).thenReturn(100);
        when(databaseService.queryForObject("select count(*) from testTable", Integer.class)).thenReturn(100);
        doNothing().when(truncateRemoveStrategy).remove(anyString(), anyString());
        deleteNavigator.deleteRowsByDate(requestDto);
        verify(truncateRemoveStrategy, times(1)).isRecommended(100);
        verify(truncateRemoveStrategy, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void getStrategyTest() {
        doReturn(false).when(truncateRemoveStrategy).isRecommended(10);
        doReturn(false).when(insertRemoveStrategy).isRecommended(10);
        doReturn(false).when(partitionRemoveStrategy).isRecommended(10);
        when(databaseService.queryForObject("select count(*) from testTable where ddate < '2021-04-22'", Integer.class)).thenReturn(10);
        when(databaseService.queryForObject("select count(*) from testTable", Integer.class)).thenReturn(100);
        deleteNavigator = Mockito.spy(new DeleteNavigatorImpl(context, databaseService, removeStrategyList));
        try {
            deleteNavigator.deleteRowsByDate(requestDto);
        }
        catch (RemoveStrategyNotFoundException e) {
            return;
        }
        fail("");
    }
}
