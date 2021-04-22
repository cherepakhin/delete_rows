package ru.stm.delete_rows.service;

import ru.stm.delete_rows.dto.RequestDto;

public interface DeleteNavigator {
    void deleteRowsByDate(RequestDto requestDto);
}
