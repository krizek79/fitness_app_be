package sk.krizan.fitness_app_be.domain.workout_exercise.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;

import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {

    Page<WorkoutExercise> findAll(Specification<WorkoutExercise> specification, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<WorkoutExercise> findAllByWorkoutIdOrderByOrder(Long workoutId);
}