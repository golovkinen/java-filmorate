package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy=PresentOrAfterValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface PresentOrAfter {
    String message() default "должен быть позже {value} и не в будущем";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value();
}

