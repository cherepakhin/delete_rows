package ru.stm.delete_rows.service;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;

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
}