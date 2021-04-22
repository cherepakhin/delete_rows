package ru.stm.delete_rows.service;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;

import static java.lang.String.format;
import static org.mockito.Mockito.*;
import static ru.stm.delete_rows.constants.Queries.SELECT_COUNT_OF_RECORDS_BY_DATE;

class DeleteServiceTest {

    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    DataSource dataSource = mock(DataSource.class);

    @Test
    void createTable() {
        DeleteService spyService = spy(new DeleteService(dataSource));
        ReflectionTestUtils.setField(spyService, "jdbcTemplate", jdbcTemplate);
        String nameTable = "testtable";
        Integer length = 100;
        doNothing().when(spyService).generateRows(nameTable, length);
        spyService.createTable(nameTable, length);
        verify(spyService, times(1)).generateRows(nameTable, length);
        verify(jdbcTemplate, times(1))
                .execute("create table testtable (id SERIAL PRIMARY KEY, ddate timestamp )");
    }

    @Test
    void dropTable() {
        DeleteService spyService = spy(new DeleteService(dataSource));
        ReflectionTestUtils.setField(spyService, "jdbcTemplate", jdbcTemplate);
        String nameTable = "testtable";
        spyService.dropTable(nameTable);
        verify(jdbcTemplate, times(1))
                .execute("drop table " + nameTable);
    }

    @Test
    void methodDeleteFromSelectForEmptyTable() {
        DeleteService spyService = spy(new DeleteService(dataSource));
        ReflectionTestUtils.setField(spyService, "jdbcTemplate", jdbcTemplate);
        String nameTable = "testtable";
        String fromDate = "fromDate";
        Integer portion = 100;
        String sql = format(SELECT_COUNT_OF_RECORDS_BY_DATE, nameTable, fromDate);
        when(jdbcTemplate.queryForObject(sql, Integer.class)).thenReturn(0);
        spyService.methodDeleteFromSelect(nameTable, fromDate, portion);
        verify(jdbcTemplate, times(1)).queryForObject(sql, Integer.class);
        verify(jdbcTemplate, times(0)).execute(anyString());
    }

    @Test
    void methodDeleteFromSelect() {
        DeleteService spyService = spy(new DeleteService(dataSource));
        ReflectionTestUtils.setField(spyService, "jdbcTemplate", jdbcTemplate);
        String nameTable = "testtable";
        String fromDate = "fromDate";
        Integer portion = 100;
        String sql = format(SELECT_COUNT_OF_RECORDS_BY_DATE, nameTable, fromDate);
        when(jdbcTemplate.queryForObject(sql, Integer.class)).thenReturn(200);
        spyService.methodDeleteFromSelect(nameTable, fromDate, portion);
        verify(jdbcTemplate, times(1)).queryForObject(sql, Integer.class);
        verify(jdbcTemplate, times(3)).execute(anyString());
    }
}