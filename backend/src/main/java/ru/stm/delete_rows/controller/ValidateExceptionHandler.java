package ru.stm.delete_rows.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.stm.delete_rows.dto.ApiFieldValidationError;

/**
 * Обработчик ошибок валидации запроса
 */
@RestControllerAdvice
public class ValidateExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiFieldValidationError apiError = new ApiFieldValidationError(status, ex.getMessage());
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> apiError.addError(fieldError.getField(), fieldError.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(fieldError -> apiError.addError(fieldError.getObjectName(), fieldError.getDefaultMessage()));

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }
}
