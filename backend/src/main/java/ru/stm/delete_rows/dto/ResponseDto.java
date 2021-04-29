package ru.stm.delete_rows.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel(description = "Модель ответа от сервера при выполнении запроса на удаление записей")
public class ResponseDto {
    @ApiModelProperty(dataType = "String", name = "status",
            notes = "Статус ответа")
    private HttpStatus status;
    @ApiModelProperty(dataType = "int", name = "rowsDeleted",
            notes = "Количество удаленных записей")
    private int rowsDeleted;
    @ApiModelProperty(dataType = "String", name = "time",
            notes = "Время в мс затраченное на удаление данных")
    private String time;
    @ApiModelProperty(dataType = "String", name = "exception",
            notes = "Сообщение об ошибке")
    private String exception;
}
