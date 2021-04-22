package ru.stm.delete_rows.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
    private String tableName;
    private String date;
    private int portion = 10000;
}
