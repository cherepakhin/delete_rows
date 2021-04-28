package ru.stm.delete_rows.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stm.delete_rows.service.utils.DeleteUtilsService;

@RestController
@RequestMapping("/api")
@Slf4j
@Api(value = "DeleteUtilsController" , tags = {"Delete Utils Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Delete Utils Controller", description = "Вспомогательное апи для быстрого создания " +
                "и удаления таблицы с константными полями")
})
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
    @PostMapping("/create_table")
    @ResponseBody
    @ApiOperation(value = "Создает новую таблицу в базе данных с указанным именем и кол-вом строк",
            notes = "Выполняет создание таблицы, с 2умя колонками: id serial, ddate timestamp",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное создание таблицы", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Некорректный запрос", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Ресурс не найден", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервиса", response = ResponseEntity.class) })
    public ResponseEntity<String> createTable(@ApiParam(name = "table", value = "Имя таблицы", type = "String", required = true)
                                              @RequestParam String table,
                                              @ApiParam(name = "length", value = "Кол-во строк при создании", defaultValue = "10",
                                                      type = "Integer")
                                              @RequestParam(defaultValue = "10") Integer length
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
    @PostMapping("/drop_table")
    @ResponseBody
    @ApiOperation(value = "Удаляет таблицу с заданным именем",
            notes = "Выполняет удаление таблицы из базы данных в соответсвии с переданным именем",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное удаление таблицы", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Некорректный запрос", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Ресурс не найден", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервиса", response = ResponseEntity.class) })
    public ResponseEntity<String> dropTable(@ApiParam(name = "table", value = "Имя таблицы",
            type = "String", required = true) @RequestParam String table) {
        try {
            deleteUtilsService.dropTable(table);
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(OK, HttpStatus.OK);
    }
}
