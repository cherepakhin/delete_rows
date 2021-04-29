package ru.stm.delete_rows.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.stm.delete_rows.aspect.annotation.ValidDate;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ApiModel(description = "Модель запроса для удаления записей из бд")
public class RequestDto {
    @NotBlank(message = "Имя таблицы не должно быть пустым")
    @ApiModelProperty(dataType = "String", name = "tableName", required = true,
            notes = "Имя таблицы из которой производится удаление данных")
    private String tableName;
    @ValidDate(dateFormat = "yyyy-MM-dd", message = "Формат передаваемой даты должен быть yyyy-MM-dd")
    @ApiModelProperty(dataType = "String", name = "date", required = true,
            notes = "Дата в формате yyyy-MM-dd по которой производится выборка устаревших записей")
    private String date;
}
