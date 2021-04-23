package ru.stm.delete_rows.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;
import ru.stm.delete_rows.service.DeleteNavigator;
import ru.stm.delete_rows.service.DeleteService;

import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("/")
@Slf4j
public class DeleteController {

    private static final String OK = "OK";
    private static final String ERROR = "ERROR:{}";

    public final DeleteService deleteService;
    public final DeleteNavigator deleteNavigator;

    public DeleteController(DeleteService deleteService, DeleteNavigator deleteNavigator) {
        this.deleteService = deleteService;
        this.deleteNavigator = deleteNavigator;
    }

    /**
     * Генерация таблицы в БД
     *
     * @param table  имя таблицы
     * @param length кол-во записей
     */
    @PostMapping("create_table")
    @ResponseBody
    public ResponseEntity<String> createTable(@RequestParam String table,
                                              @RequestParam Integer length
    ) {
        try {
            deleteService.createTable(table, length);
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(OK, HttpStatus.OK);
    }

    /**
     * Удаление таблицы
     *
     * @param table имя таблицы
     */
    @PostMapping("drop_table")
    @ResponseBody
    public ResponseEntity<String> dropTable(@RequestParam String table) {
        try {
            deleteService.dropTable(table);
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(OK, HttpStatus.OK);
    }

    /**
     * Удаление через delete from table where id in (select id from table where ... limit portion
     *
     * @param table    имя таблицы
     * @param fromDate удалять старее этой даты
     * @param portion  удалять порциями по portion рядов
     */
    @PostMapping("in_select")
    public ResponseEntity<String> methodDeleteFromSelect(@RequestParam @NotNull String table,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                         @NotNull
                                                         @RequestParam Date fromDate,
                                                         @RequestParam(defaultValue = "10000") Integer portion
    ) {
        try {
            deleteService.methodDeleteFromSelect(table, String.valueOf(fromDate), portion);
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(OK, HttpStatus.OK);
    }

    /**
     * Удаление через стратегию выбора метода
     *
     * @param requestDto параметры запроса(таблица)
     */
    @PostMapping("delete")
    public ResponseDto methodDeleteFromSelect(@RequestBody RequestDto requestDto) {
        ResponseDto responseDto = new ResponseDto();
        try {
            responseDto = deleteNavigator.deleteRowsByDate(requestDto);
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            return responseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setException(e.getMessage());
        }
        return responseDto.setStatus(HttpStatus.OK);
    }
}
