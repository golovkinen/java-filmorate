package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Payload;
import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy=AfterDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface AfterDate {

    String message() default "должен быть позже {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value();

}
