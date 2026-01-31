package sk.krizan.fitness_app_be.domain.workout_exercise.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.order.OrderableEntity;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.common.order.AbstractOrderableEntityOrderService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkoutExerciseOrderService extends AbstractOrderableEntityOrderService<WorkoutExercise> {

    private final WorkoutExerciseRepository workoutExerciseRepository;


    @Override
    protected Object getParent(WorkoutExercise entity) {
        return entity.getWorkout();
    }

    @Override
    protected List<WorkoutExercise> findAllSiblings(WorkoutExercise entity) {
        return workoutExerciseRepository.findAllByWorkoutIdOrderByOrder(entity.getWorkout().getId());
    }

    @Override
    protected void saveAll(List<WorkoutExercise> entities) {
        workoutExerciseRepository.saveAll(entities);
    }

    @Override
    public Class<? extends OrderableEntity> supportedClass() {
        return WorkoutExercise.class;
    }
}
