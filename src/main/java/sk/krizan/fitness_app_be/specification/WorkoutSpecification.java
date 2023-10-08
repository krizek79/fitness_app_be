package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.util.PredicateUtils;

public class WorkoutSpecification {

    public static Specification<Workout> filter(WorkoutFilterRequest request) {
        return (Root<Workout> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            Predicate namePredicate = PredicateUtils.sanitizedLike(
                root,
                criteriaBuilder,
                Workout.Fields.name,
                request.name());
            predicate = criteriaBuilder.and(predicate, namePredicate);

            Predicate levelPredicate = criteriaBuilder.equal(
                root.get(Workout.Fields.level),
                request.levelKey());
            predicate = criteriaBuilder.and(predicate, levelPredicate);

            // Filter by tags

            return predicate;
        };
    }
}
