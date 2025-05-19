package sk.krizan.fitness_app_be.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

public class WorkoutExerciseSpecification {

    public static Specification<WorkoutExercise> filter(WorkoutExerciseFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.workoutId() != null) {
                Join<WorkoutExercise, Workout> workoutJoin = root.join(WorkoutExercise.Fields.workout);
                Predicate workoutIdPredicate = criteriaBuilder.equal(workoutJoin.get(Workout.Fields.id), request.workoutId());
                predicate = criteriaBuilder.and(predicate, workoutIdPredicate);
            }

            return predicate;
        };
    }
}
