package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PresentOrAfterValidator implements ConstraintValidator <PresentOrAfter, LocalDate>{
    private LocalDate date;

    public void initialize(PresentOrAfter annotation) {
        date = LocalDate.parse(annotation.value());
    }

    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (value.isBefore(date) || value.isAfter(LocalDate.now())) {
                valid = false;
            }
        }
        return valid;
    }
}
