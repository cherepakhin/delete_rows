package ru.stm.delete_rows.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

class DeleteServiceTest {

    DatabaseService databaseService = mock(DatabaseService.class);
    DeleteService spyService;

    @BeforeEach
    void setUp() {
        spyService = spy(new DeleteService(databaseService));
        ReflectionTestUtils.setField(spyService, "databaseService", databaseService);
    }

    @AfterEach
    void tearDown() {
        spyService = null;
    }

    @Test
    void createTable() {

        String nameTable = "testtable";
        Integer length = 100;
        doNothing().when(spyService).generateRows(nameTable, length);
        spyService.createTable(nameTable, length);
        verify(spyService, times(1)).generateRows(nameTable, length);
        verify(databaseService, times(1))
                .execute("create table if not exists testtable (id SERIAL PRIMARY KEY, ddate timestamp )");
    }

    @Test
    void dropTable() {
        String nameTable = "testtable";
        spyService.dropTable(nameTable);
        verify(databaseService, times(1))
                .execute("drop table testtable");
    }

    @Test
    void methodDeleteFromSelectForEmptyTable() {
        String nameTable = "testtable";
        String fromDate = "fromDate";
        Integer portion = 100;
        String sql = "select count(*) from testtable where ddate < 'fromDate'";
        when(databaseService.queryForObject(sql, Integer.class)).thenReturn(0);
        spyService.methodDeleteFromSelect(nameTable, fromDate, portion);
        verify(databaseService, times(1)).queryForObject(sql, Integer.class);
        verify(databaseService, times(0)).execute(anyString());
    }

    @Test
    void methodDeleteFromSelect() {
        String nameTable = "testtable";
        String fromDate = "fromDate";
        Integer portion = 100;
        String sql = "select count(*) from testtable where ddate < 'fromDate'";
        when(databaseService.queryForObject(sql, Integer.class)).thenReturn(200);
        spyService.methodDeleteFromSelect(nameTable, fromDate, portion);
        verify(databaseService, times(1)).queryForObject(sql, Integer.class);
        verify(databaseService, times(3)).execute(anyString());
    }
}