package ru.stm.delete_rows.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.service.DeleteNavigator;
import ru.stm.delete_rows.service.DeleteService;

@RestController
@RequestMapping("/")
@Slf4j
public class DeleteController {

    private static final String OK = "OK";

    DeleteService deleteService;
    DeleteNavigator deleteNavigator;

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
            log.error("Error:{}", e.getMessage());
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
            log.error("Error:{}", e.getMessage());
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
    public ResponseEntity<String> methodDeleteFromSelect(@RequestParam String table,
                                                         @RequestParam String fromDate,
                                                         @RequestParam Integer portion
    ) {
        try {
            deleteService.methodDeleteFromSelect(table, fromDate, portion);
        } catch (Exception e) {
            log.error("Error:{}", e.getMessage());
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
    public ResponseEntity<String> methodDeleteFromSelect(@RequestBody RequestDto requestDto) {
        try {
            deleteNavigator.deleteRowsByDate(requestDto);
        } catch (Exception e) {
            log.error("Error:{}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(OK, HttpStatus.OK);
    }
}
