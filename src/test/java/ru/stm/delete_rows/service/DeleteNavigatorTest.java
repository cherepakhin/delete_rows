package ru.stm.delete_rows.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.impl.DeleteNavigatorImpl;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

public class DeleteNavigatorTest {
    DeleteMethodContext context = mock(DeleteMethodContext.class);
    DatabaseService databaseService = mock(DatabaseService.class);
    List<RemoveStrategy> removeStrategyList = new ArrayList<>();
    RequestDto requestDto;

    @BeforeEach
    public void setUp() {
        doNothing().when(context).setStrategy(any());
        doNothing().when(context).execute(anyString(), anyString(), anyInt());
        doNothing().when(databaseService).execute(anyString());
        when(databaseService.queryForObject(anyString(), any())).thenReturn(null);
        requestDto = new RequestDto();
        requestDto.setTableName("testTable");
        requestDto.setDate(new Date(System.currentTimeMillis()));
    }

    @AfterEach
    public void tearDown() {
        requestDto = null;
    }

    @Test
    public void deleteRowsByDateTest() throws Exception {
        DeleteNavigator deleteNavigator = PowerMockito.spy(new DeleteNavigatorImpl(context, databaseService, removeStrategyList));
        PowerMockito
                .when(deleteNavigator, method(DeleteNavigatorImpl.class, "getCountOfRecordsToRemove",
                        String.class,
                        String.class))
                .withArguments(requestDto.getTableName(), String.valueOf(requestDto.getDate())).thenReturn(100);
        PowerMockito
                .when(deleteNavigator, method(DeleteNavigatorImpl.class, "getCountOfRecords",
                        String.class))
                .withArguments(requestDto.getTableName()).thenReturn(200);
        //todo
       /* PowerMockito.doNothing().when(deleteNavigator, method(DeleteNavigatorImpl.class,
                "calculatePercentage", Integer.class, Integer.class)).withArguments(anyInt(), anyInt());
        /*PowerMockito
                .when(deleteNavigator, method(DeleteNavigatorImpl.class, "getStrategy",
                        String.class,
                        Integer.class))
                .withArguments(anyString(), anyInt()).thenReturn(null);*/

       // deleteNavigator.deleteRowsByDate(requestDto);
        //assertEquals(50, response);
        //    verifyPrivate(spy, times(1)).invoke("anotherPrivateMethod", "xyz");
    }
}
