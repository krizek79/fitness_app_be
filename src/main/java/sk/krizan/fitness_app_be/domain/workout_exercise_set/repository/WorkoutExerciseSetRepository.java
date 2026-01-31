package sk.krizan.fitness_app_be.domain.workout_exercise_set.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;

import java.util.List;

@Repository
public interface WorkoutExerciseSetRepository extends JpaRepository<WorkoutExerciseSet, Long> {

    Page<WorkoutExerciseSet> findAll(Specification<WorkoutExerciseSet> specification, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<WorkoutExerciseSet> findAllByWorkoutExerciseIdOrderByOrder(Long workoutExerciseId);
}
