package ru.stm.delete_rows.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stm.delete_rows.dto.RequestDto;
import ru.stm.delete_rows.dto.ResponseDto;
import ru.stm.delete_rows.service.DeleteNavigator;

import javax.validation.Valid;

import static ru.stm.delete_rows.controller.DeleteUtilsController.ERROR;

@RestController
@RequestMapping("/api")
@Slf4j
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
