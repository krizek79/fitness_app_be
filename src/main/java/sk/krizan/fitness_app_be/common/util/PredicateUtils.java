package sk.krizan.fitness_app_be.common.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateUtils {

    public static <T> Predicate sanitizedLike(Root<T> root, CriteriaBuilder criteriaBuilder, String fieldName, String value) {
        return sanitizedLike(criteriaBuilder, root.get(fieldName), value);
    }

    public static Predicate sanitizedLike(CriteriaBuilder criteriaBuilder, Expression<String> fieldExpression, String value) {
        if (value == null) {
            return criteriaBuilder.conjunction();
        } else {
            String sanitizedValue = "%" + value.replaceAll("\\s+", "").toLowerCase() + "%";

            Expression<String> sanitizedFieldExpression = criteriaBuilder.lower(
                    criteriaBuilder.function(
                            "REPLACE",
                            String.class,
                            fieldExpression,
                            criteriaBuilder.literal(" "),
                            criteriaBuilder.literal("")));

            return criteriaBuilder.like(sanitizedFieldExpression, criteriaBuilder.literal(sanitizedValue));
        }
    }


}
