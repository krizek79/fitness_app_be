package sk.krizan.fitness_app_be.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PredicateUtils {

    public static <T> Predicate sanitizedLike(
        Root<T> root, CriteriaBuilder criteriaBuilder, String fieldName, String value) {
        if (value == null) {
            return criteriaBuilder.conjunction();
        } else {
            String sanitizedValue =
                "%" + value.replaceAll("\\s+", "").toLowerCase() + "%";

            Expression<String> sanitizedFieldExpression = criteriaBuilder.lower(
                criteriaBuilder.function(
                    "REPLACE",
                    String.class,
                    root.get(fieldName),
                    criteriaBuilder.literal(" "),
                    criteriaBuilder.literal("")));
            Expression<String> sanitizedValueExpression = criteriaBuilder.literal(sanitizedValue);

            return criteriaBuilder.like(sanitizedFieldExpression, sanitizedValueExpression);
        }
    }
}
