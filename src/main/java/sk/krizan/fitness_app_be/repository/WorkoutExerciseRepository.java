package sk.krizan.fitness_app_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {

    Page<WorkoutExercise> findAll(Specification<WorkoutExercise> specification, Pageable pageable);
    List<WorkoutExercise> findAllByWorkoutIdAndIdNotOrderByOrder(Long workoutId, Long excludedWorkoutExerciseId);
    List<WorkoutExercise> findAllByWorkoutIdOrderByOrder(Long workoutId);
}