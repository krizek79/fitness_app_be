package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;

public class WorkoutExerciseSetSpecification {

    public static Specification<WorkoutExerciseSet> filter(WorkoutExerciseSetFilterRequest request) {
        return (Root<WorkoutExerciseSet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.workoutExerciseId() != null) {
                Join<WorkoutExerciseSet, WorkoutExercise> workoutExerciseJoin = root.join(WorkoutExerciseSet.Fields.workoutExercise);
                Predicate workoutExerciseIdPredicate = criteriaBuilder.equal(workoutExerciseJoin.get(WorkoutExercise.Fields.id), request.workoutExerciseId());
                predicate = criteriaBuilder.and(predicate, workoutExerciseIdPredicate);
            }

            return predicate;
        };
    }
}
