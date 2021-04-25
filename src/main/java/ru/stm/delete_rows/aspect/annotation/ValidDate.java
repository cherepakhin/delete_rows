package ru.stm.delete_rows.aspect.annotation;

import ru.stm.delete_rows.aspect.ValidDateConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateConstraint.class)
@Documented
public @interface ValidDate {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    String dateFormat();
}
