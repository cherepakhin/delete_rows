package ru.stm.delete_rows.service;

import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;

public interface DeleteNavigator {
    ResponseDto deleteRowsByDate(RequestDto requestDto);
}
