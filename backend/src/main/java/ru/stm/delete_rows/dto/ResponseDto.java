package ru.stm.delete_rows.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Accessors(chain = true)
public class ResponseDto {
    private HttpStatus status;
    private int rowsDeleted;
    private String time;
    private String exception;
}
