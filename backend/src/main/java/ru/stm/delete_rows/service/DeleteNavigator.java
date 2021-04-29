package ru.stm.delete_rows.service;

import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;

/**
 * Сервис удаления устаревших записей из таблицы
 */
public interface DeleteNavigator {
    /**
     * Удаляет записи из таблицы в соответсвии с переданными параметрами
     * @param requestDto запрос с параметрами для удаления данных
     * @return возвращает информацию об операции удаления
     */
    ResponseDto deleteRowsByDate(RequestDto requestDto);
}
