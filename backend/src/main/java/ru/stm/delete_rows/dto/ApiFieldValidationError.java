package ru.stm.delete_rows.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Модель ответа для обработки исключений при валидации запроса")
public class ApiFieldValidationError {
    @ApiModelProperty(dataType = "HttpStatus", name = "status", required = true,
            notes = "Статус ответа")
    private HttpStatus status;
    @ApiModelProperty(dataType = "String", name = "message", required = true,
            notes = "Сообщение об ошибке")
    private String message;
    @ApiModelProperty(dataType = "List", name = "errors", required = true,
            notes = "Список ошибок валидации")
    private List<ValidationField> errors = new ArrayList<>();

    public ApiFieldValidationError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public void addError(String field, String error) {
        this.errors.add(new ValidationField(field, error));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "Модель описания поля валидации")
    public static class ValidationField {
        @ApiModelProperty(dataType = "String", name = "field", required = true,
                notes = "Поле не прошедшее валидацию")
        private String field;
        @ApiModelProperty(dataType = "String", name = "error", required = true,
                notes = "Сообщение об ошибке")
        private String error;
    }
}
