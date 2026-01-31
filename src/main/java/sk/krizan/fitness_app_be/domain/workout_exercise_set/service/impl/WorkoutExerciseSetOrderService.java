package sk.krizan.fitness_app_be.domain.workout_exercise_set.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.order.OrderableEntity;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.repository.WorkoutExerciseSetRepository;
import sk.krizan.fitness_app_be.common.order.AbstractOrderableEntityOrderService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkoutExerciseSetOrderService extends AbstractOrderableEntityOrderService<WorkoutExerciseSet> {

    private final WorkoutExerciseSetRepository repository;

    @Override
    protected Object getParent(WorkoutExerciseSet entity) {
        return entity.getWorkoutExercise();
    }

    @Override
    protected List<WorkoutExerciseSet> findAllSiblings(WorkoutExerciseSet entity) {
        return repository.findAllByWorkoutExerciseIdOrderByOrder(entity.getWorkoutExercise().getId());
    }

    @Override
    protected void saveAll(List<WorkoutExerciseSet> entities) {
        repository.saveAll(entities);
    }

    @Override
    public Class<? extends OrderableEntity> supportedClass() {
        return WorkoutExerciseSet.class;
    }
}
