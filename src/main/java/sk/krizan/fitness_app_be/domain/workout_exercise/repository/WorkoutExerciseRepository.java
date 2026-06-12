package sk.krizan.fitness_app_be.domain.workout_exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {

    default WorkoutExercise getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, WorkoutExercise.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}