package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
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

            if (request.muscleGroupList() != null && !request.muscleGroupList().isEmpty()) {
                SetJoin<Exercise, MuscleGroup> muscleGroupJoin = root.joinSet(Exercise.Fields.muscleGroupSet);

                query.groupBy(root.get(Exercise.Fields.id));
                Predicate havingPredicate = criteriaBuilder.equal(
                        criteriaBuilder.countDistinct(muscleGroupJoin),
                        request.muscleGroupList().size()
                );
                Predicate inPredicate = muscleGroupJoin.in(request.muscleGroupList());
                query.having(criteriaBuilder.and(havingPredicate));
                predicate = criteriaBuilder.and(predicate, inPredicate);
            }

            return predicate;
        };
    }
}
