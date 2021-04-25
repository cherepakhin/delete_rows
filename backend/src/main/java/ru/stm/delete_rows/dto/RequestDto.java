package ru.stm.delete_rows.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.stm.delete_rows.aspect.annotation.ValidDate;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
public class RequestDto {
    @NotBlank(message = "Имя таблицы не должно быть пустым")
    private String tableName;
    @ValidDate(dateFormat = "yyyy-MM-dd", message = "Формат передаваемой даты должен быть yyyy-MM-dd")
    private String date;
}
