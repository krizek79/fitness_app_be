package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;

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
    protected List<WorkoutExercise> findAllSiblingsExcludingSelf(WorkoutExercise entity) {
        return workoutExerciseRepository.findAllByWorkoutIdAndIdNotOrderByOrder(entity.getWorkout().getId(), entity.getId());
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
