package ru.stm.delete_rows.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class RequestDto {
    @NotNull(message = "Имя таблицы не задано")
    @NotBlank(message = "Имя таблицы не должно быть пустым")
    private String tableName;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @NotNull(message = "Укажите дату в формате yyyy-MM-dd hh:mm:ss (ex 2021-05-12 12:00:31")
    private Date date;
    private int portion = 10000;
}
