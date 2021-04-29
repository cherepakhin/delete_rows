package ru.stm.delete_rows.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stm.delete_rows.dto.ApiFieldValidationError;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;
import ru.stm.delete_rows.service.DeleteNavigator;

import javax.validation.Valid;

import static ru.stm.delete_rows.controller.DeleteUtilsController.ERROR;

@RestController
@RequestMapping("/api")
@Slf4j
@Api(value = "DeleteController" , tags = {"Delete Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Delete Controller", description = "Api для удаления старых записей из базы данных")
})
public class DeleteController {

    public final DeleteNavigator deleteNavigator;

    public DeleteController(DeleteNavigator deleteNavigator) {
        this.deleteNavigator = deleteNavigator;
    }

    /**
     * Удаление через стратегию выбора метода
     *
     * @param requestDto параметры запроса(таблица)
     */
    @ApiOperation(value = "Удаляет строки из бд старше переданной даты",
            notes = "Выполняет удаление строк из указанной таблицы в базе данных по определенной стратегии, "+
                    "взависимости от количества удаляемых данных",
            response = ResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное удаление данных", response = ResponseDto.class),
            @ApiResponse(code = 400, message = "Некорректный запрос. Неуспешная валидация",
                         response = ApiFieldValidationError.class),
            @ApiResponse(code = 404, message = "Ресурс не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервиса") })
    @PostMapping("/delete")
    public ResponseEntity<ResponseDto> methodDeleteFromSelect(@Valid @RequestBody RequestDto requestDto) {
        ResponseDto responseDto = new ResponseDto();
        try {
            responseDto = deleteNavigator.deleteRowsByDate(requestDto);
        } catch (Exception e) {
            log.error(ERROR, e.getMessage());
            responseDto
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setException(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
        return ResponseEntity.ok(responseDto.setStatus(HttpStatus.OK));
    }
}
