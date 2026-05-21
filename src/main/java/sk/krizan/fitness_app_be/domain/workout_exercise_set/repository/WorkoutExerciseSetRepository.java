package sk.krizan.fitness_app_be.domain.workout_exercise_set.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;

@Repository
public interface WorkoutExerciseSetRepository extends JpaRepository<WorkoutExerciseSet, Long> {

    Page<WorkoutExerciseSet> findAll(Specification<WorkoutExerciseSet> specification, Pageable pageable);

    default WorkoutExerciseSet getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, WorkoutExerciseSet.class.getSimpleName() + " with id { %s } does not exist.".formatted(id)));
    }
}
