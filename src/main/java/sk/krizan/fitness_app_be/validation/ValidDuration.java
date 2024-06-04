package sk.krizan.fitness_app_be.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DurationValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDuration {

    String message() default "Invalid duration format. Expected format is 'PT#H#M#S'.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
