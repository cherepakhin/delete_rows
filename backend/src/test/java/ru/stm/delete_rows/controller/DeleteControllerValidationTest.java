package ru.stm.delete_rows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.stm.delete_rows.dto.ApiFieldValidationError;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;
import ru.stm.delete_rows.service.DeleteNavigator;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteController.class)
class DeleteControllerValidationTest {
    ObjectMapper objectMapper = new ObjectMapper();
    RequestDto requestDto = new RequestDto();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeleteNavigator navigator;

    private ResultActions executePost(RequestDto request, ResultMatcher status) throws Exception {
        return this.mockMvc.perform(post("/api/delete")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status);
    }

    @Test
    void deleteRequestDatePositiveTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("2021-04-22");
        requestDto.setTableName("testTable");
        executePost(requestDto, status().isOk());
        verify(navigator, times(1)).deleteRowsByDate(requestDto);
    }

    @Test
    void deleteRequestDateNullTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setTableName("testTable");
        MvcResult mvcResult = executePost(requestDto, status().isBadRequest()).andReturn();
        verify(navigator, times(0)).deleteRowsByDate(requestDto);
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiFieldValidationError error = objectMapper.readValue(content, ApiFieldValidationError.class);
        Assertions.assertEquals(1, error.getErrors().size());
        Assertions.assertEquals("date",
                error.getErrors().get(0).getField());
        Assertions.assertEquals("Укажите дату в формате yyyy-MM-dd",
                error.getErrors().get(0).getError());
    }

    @Test
    void deleteRequestIncorrectPatternDateTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("2021^04^22");
        requestDto.setTableName("testTable");
        MvcResult mvcResult = executePost(requestDto, status().isBadRequest()).andReturn();
        verify(navigator, times(0)).deleteRowsByDate(requestDto);
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiFieldValidationError error = objectMapper.readValue(content, ApiFieldValidationError.class);
        Assertions.assertEquals(1, error.getErrors().size());
        Assertions.assertEquals("date",
                error.getErrors().get(0).getField());
        Assertions.assertEquals("Некорректный формат даты! Укажите дату в формате: yyyy-MM-dd",
                error.getErrors().get(0).getError());
    }

    @Test
    void deleteRequestEmptyDateTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("");
        requestDto.setTableName("testTable");
        MvcResult mvcResult = executePost(requestDto, status().isBadRequest()).andReturn();
        verify(navigator, times(0)).deleteRowsByDate(requestDto);
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiFieldValidationError error = objectMapper.readValue(content, ApiFieldValidationError.class);
        Assertions.assertEquals(1, error.getErrors().size());
        Assertions.assertEquals("date",
                error.getErrors().get(0).getField());
        Assertions.assertEquals("Некорректный формат даты! Укажите дату в формате: yyyy-MM-dd",
                error.getErrors().get(0).getError());
    }

    @Test
    void deleteRequestEmptyTableNameTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("2021-04-21");
        requestDto.setTableName("");
        MvcResult mvcResult = executePost(requestDto, status().isBadRequest()).andReturn();
        verify(navigator, times(0)).deleteRowsByDate(requestDto);
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiFieldValidationError error = objectMapper.readValue(content, ApiFieldValidationError.class);
        Assertions.assertEquals(1, error.getErrors().size());
        Assertions.assertEquals("tableName",
                error.getErrors().get(0).getField());
        Assertions.assertEquals("Имя таблицы не должно быть пустым",
                error.getErrors().get(0).getError());
    }

    @Test
    void deleteRequestNullTableNameTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("2021-04-21");
        MvcResult mvcResult = executePost(requestDto, status().isBadRequest()).andReturn();
        verify(navigator, times(0)).deleteRowsByDate(requestDto);
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiFieldValidationError error = objectMapper.readValue(content, ApiFieldValidationError.class);
        Assertions.assertEquals(1, error.getErrors().size());
        Assertions.assertEquals("tableName",
                error.getErrors().get(0).getField());
        Assertions.assertEquals("Имя таблицы не должно быть пустым",
                error.getErrors().get(0).getError());
    }

    @Test
    void deleteRequestNullTableNameAndIncorrectDataTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("abcd-ef-gh");
        MvcResult mvcResult = executePost(requestDto, status().isBadRequest()).andReturn();
        verify(navigator, times(0)).deleteRowsByDate(requestDto);
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiFieldValidationError error = objectMapper.readValue(content, ApiFieldValidationError.class);
        List<ApiFieldValidationError.ValidationField> fields = error.getErrors();
        Assertions.assertEquals(2, fields.size());
        ApiFieldValidationError.ValidationField dateFiled = fields
                .stream()
                .filter(item -> item.getField().equals("date"))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(dateFiled);
        Assertions.assertEquals("Некорректный формат даты! Укажите дату в формате: yyyy-MM-dd",
                dateFiled.getError());
        ApiFieldValidationError.ValidationField tableNameField = fields
                .stream()
                .filter(item -> item.getField().equals("tableName"))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(tableNameField);
        Assertions.assertEquals("Имя таблицы не должно быть пустым",
                tableNameField.getError());
    }
}
