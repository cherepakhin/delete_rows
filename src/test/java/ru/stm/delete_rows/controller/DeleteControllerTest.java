package ru.stm.delete_rows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;
import ru.stm.delete_rows.service.DeleteNavigator;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteController.class)
class DeleteControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    RequestDto requestDto = new RequestDto();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeleteNavigator navigator;

    @Test
    void deletePositiveTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("2021-04-22");
        requestDto.setTableName("testTable");
        this.mockMvc.perform(post("/delete")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(navigator, times(1)).deleteRowsByDate(requestDto);
    }

    @Test
    void deleteNegativeTest() throws Exception {
        when(navigator.deleteRowsByDate(any())).thenReturn(new ResponseDto());
        requestDto.setDate("2021-04-22");
        requestDto.setTableName("testTable");
        doThrow(IllegalArgumentException.class).when(navigator).deleteRowsByDate(requestDto);
        this.mockMvc.perform(post("/delete")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(navigator, times(1)).deleteRowsByDate(requestDto);
    }
}
