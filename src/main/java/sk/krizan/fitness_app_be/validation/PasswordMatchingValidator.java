package sk.krizan.fitness_app_be.validation;

import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, Object> {

    private String field;
    private String matchField;

    @Override
    public void initialize(PasswordMatching constraintAnnotation) {
        field = constraintAnnotation.password();
        matchField = constraintAnnotation.matchingPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object matchFieldValue = new BeanWrapperImpl(value).getPropertyValue(matchField);

        return Objects.equals(fieldValue, matchFieldValue);
    }
}
