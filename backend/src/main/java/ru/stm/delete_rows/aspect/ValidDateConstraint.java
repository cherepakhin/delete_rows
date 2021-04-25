package ru.stm.delete_rows.aspect;

import lombok.extern.slf4j.Slf4j;
import ru.stm.delete_rows.aspect.annotation.ValidDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Slf4j
public class ValidDateConstraint implements ConstraintValidator<ValidDate, String> {
    private String dateFormat;

    @Override
    public void initialize(ValidDate constraint) {
        dateFormat = constraint.dateFormat();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Укажите дату в формате yyyy-MM-dd")
                    .addConstraintViolation();
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            format.parse(value);
            return true;
        } catch (ParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Некорректный формат даты! Укажите дату в формате: %s", dateFormat))
                    .addConstraintViolation();
        }
        return false;
    }
}
