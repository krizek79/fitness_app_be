package sk.krizan.fitness_app_be.domain.workout_exercise_set_result.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.entity.WorkoutExerciseSetResult;

@Repository
public interface WorkoutExerciseSetResultRepository extends JpaRepository<WorkoutExerciseSetResult, Long> {

    default WorkoutExerciseSetResult getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, WorkoutExerciseSetResult.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}
