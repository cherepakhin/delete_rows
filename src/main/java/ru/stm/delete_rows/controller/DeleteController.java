package ru.stm.delete_rows.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stm.delete_rows.service.DeleteService;

@RestController
@RequestMapping("/")
@Slf4j
public class DeleteController {

    DeleteService deleteService;

    public DeleteController(DeleteService deleteService) {
        this.deleteService = deleteService;
    }

    /**
     * Генерация таблицы в БД
     *
     * @param table  имя таблицы
     * @param length кол-во записей
     */
    @PostMapping("new_table")
    @ResponseBody
    public ResponseEntity createTable(@RequestParam String table,
                                      @RequestParam Integer length
    ) {
        try {
            deleteService.createTable(table, length);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Удаление таблицы
     *
     * @param table имя таблицы
     */
    @PostMapping("drop_table")
    @ResponseBody
    public ResponseEntity dropTable(@RequestParam String table) {
        try {
            deleteService.dropTable(table);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Удаление через delete from table where id in (select id from table where ... limit portion
     *
     * @param deleteRequest параметры удаления
     */
    @PostMapping("in_select")
    public ResponseEntity methodDeleteFromSelect(@RequestBody DeleteRequest deleteRequest) {
        try {
            deleteService.methodDeleteFromSelect(deleteRequest);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
