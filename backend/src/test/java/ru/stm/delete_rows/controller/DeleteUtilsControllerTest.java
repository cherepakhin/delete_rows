package ru.stm.delete_rows.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.stm.delete_rows.service.utils.DeleteUtilsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteUtilsController.class)
class DeleteUtilsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeleteUtilsService service;

    @Test
    void createTable() throws Exception {
        String nameTable = "table";
        Integer length = 100;
        this.mockMvc.perform(post("/api/create_table")
                .param("table", nameTable)
                .param("length", length.toString())
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Операция выполнена")));
        verify(service, times(1)).createTable(nameTable, length);
    }

    @Test
    void createTableException() throws Exception {
        String nameTable = "table";
        Integer length = 100;
        doThrow(IllegalArgumentException.class).when(service).createTable(nameTable, length);
        this.mockMvc.perform(post("/api/create_table")
                .param("table", nameTable)
                .param("length", length.toString())
        ).andDo(print()).andExpect(status().isInternalServerError());
        verify(service, times(1)).createTable(nameTable, length);
    }

    @Test
    void dropTable() throws Exception {
        String nameTable = "table";
        this.mockMvc.perform(post("/api/drop_table")
                .param("table", nameTable)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Операция выполнена")));
        verify(service, times(1)).dropTable(nameTable);
    }

    @Test
    void dropTableException() throws Exception {
        String nameTable = "table";
        doThrow(IllegalArgumentException.class).when(service).dropTable(nameTable);
        this.mockMvc.perform(post("/api/drop_table")
                .param("table", nameTable)
        ).andDo(print()).andExpect(status().isInternalServerError());
        verify(service, times(1)).dropTable(nameTable);
    }

    @Test
    void insertRow() throws Exception {
        String nameTable = "table";
        Integer length = 10;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter);
        LocalDateTime parsedDate = LocalDate.parse(date, formatter).atStartOfDay();
        this.mockMvc.perform(post("/api/insert_rows")
                .param("table", nameTable)
                .param("date", date)
                .param("length", String.valueOf(length))
        ).andDo(print()).andExpect(status().isOk());
        verify(service, times(1)).insertRows(nameTable, length, parsedDate);
    }

    @Test
    void insertRowException() throws Exception {
        String nameTable = "table";
        Integer length = 10;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter);
        LocalDateTime parsedDate = LocalDate.parse(date, formatter).atStartOfDay();
        doThrow(IllegalArgumentException.class).when(service).insertRows(nameTable, length, parsedDate);
        this.mockMvc.perform(post("/api/insert_rows")
                .param("table", nameTable)
                .param("date", date)
                .param("length", String.valueOf(length))
        ).andDo(print()).andExpect(status().isInternalServerError());
        verify(service, times(1)).insertRows(nameTable, length, parsedDate);
    }

}