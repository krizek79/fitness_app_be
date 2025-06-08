package sk.krizan.fitness_app_be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;

import java.util.List;

@Repository
public interface WorkoutExerciseSetRepository extends JpaRepository<WorkoutExerciseSet, Long> {

    Page<WorkoutExerciseSet> findAll(Specification<WorkoutExerciseSet> specification, Pageable pageable);
    List<WorkoutExerciseSet> findAllByWorkoutExerciseIdAndIdNotOrderByOrder(Long workoutExerciseId, Long excludedWorkoutExerciseSetId);
    List<WorkoutExerciseSet> findAllByWorkoutExerciseIdOrderByOrder(Long workoutExerciseId);
}
