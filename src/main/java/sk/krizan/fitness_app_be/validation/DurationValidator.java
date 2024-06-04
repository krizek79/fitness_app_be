package sk.krizan.fitness_app_be.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationValidator implements ConstraintValidator<ValidDuration, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        try {
            Duration duration = Duration.parse(value);
            return !duration.isNegative();
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
