package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.util.PredicateUtils;

public class WorkoutSpecification {

    public static Specification<Workout> filter(WorkoutFilterRequest request) {
        return (Root<Workout> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.name() != null) {
                Predicate namePredicate = PredicateUtils.sanitizedLike(
                        root,
                        criteriaBuilder,
                        Workout.Fields.name,
                        request.name());
                predicate = criteriaBuilder.and(predicate, namePredicate);
            }

            if (request.levelKey() != null) {
                Predicate levelPredicate = criteriaBuilder.equal(
                        root.get(Workout.Fields.level),
                        request.levelKey());
                predicate = criteriaBuilder.and(predicate, levelPredicate);
            }

            if (request.tagNameList() != null && !request.tagNameList().isEmpty()) {
                Join<Workout, Tag> tagJoin = root.join(Workout.Fields.tags);
                Predicate tagPredicate = tagJoin.get(Tag.Fields.name).in(request.tagNameList());
                predicate = criteriaBuilder.and(predicate, tagPredicate);
            }

            return predicate;
        };
    }
}
