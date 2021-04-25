package ru.stm.delete_rows.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stm.delete_rows.service.utils.DeleteUtilsService;

@RestController
@RequestMapping("/")
@Slf4j
public class DeleteUtilsController {

    public static final String OK = "OK";
    public static final String ERROR = "ERROR:{}";

    public final DeleteUtilsService deleteUtilsService;


    public DeleteUtilsController(DeleteUtilsService deleteUtilsService) {
        this.deleteUtilsService = deleteUtilsService;
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
            deleteUtilsService.createTable(table, length);
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
            deleteUtilsService.dropTable(table);
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(OK, HttpStatus.OK);
    }
}
