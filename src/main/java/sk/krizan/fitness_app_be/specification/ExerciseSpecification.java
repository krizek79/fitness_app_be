package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.util.PredicateUtils;

public class ExerciseSpecification {

    public static Specification<Exercise> filter(ExerciseFilterRequest request) {
        return (Root<Exercise> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.name() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Exercise.Fields.name,
                        request.name());
                predicate = criteriaBuilder.and(predicate, namePredicate);
            }

            if (request.muscleGroupKeyList() != null && !request.muscleGroupKeyList().isEmpty()) {
                Predicate muscleGroupPredicate = root.get(Exercise.Fields.muscleGroups)
                        .in(request.muscleGroupKeyList());
                predicate = criteriaBuilder.and(predicate, muscleGroupPredicate);
            }

            return predicate;
        };
    }
}
