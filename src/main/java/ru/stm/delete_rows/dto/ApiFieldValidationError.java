package ru.stm.delete_rows.dto;

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
public class ApiFieldValidationError {
    private HttpStatus status;
    private String message;
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
    public static class ValidationField {
        private String field;
        private String error;
    }
}
