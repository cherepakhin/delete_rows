package ru.stm.delete_rows.service.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.stm.delete_rows.service.DatabaseService;

import static org.mockito.Mockito.*;

class DeleteUtilsServiceTest {

    DatabaseService databaseService = mock(DatabaseService.class);
    DeleteUtilsService spyService;

    @BeforeEach
    void setUp() {
        spyService = spy(new DeleteUtilsService(databaseService));
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
}