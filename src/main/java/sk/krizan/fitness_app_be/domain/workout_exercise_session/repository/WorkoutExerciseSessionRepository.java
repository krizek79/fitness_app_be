package sk.krizan.fitness_app_be.domain.workout_exercise_session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;

@Repository
public interface WorkoutExerciseSessionRepository extends JpaRepository<WorkoutExerciseSession, Long> {

    default WorkoutExerciseSession getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, WorkoutExerciseSession.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}
