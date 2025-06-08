package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseSetRepository;

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
    protected List<WorkoutExerciseSet> findAllSiblingsExcludingSelf(WorkoutExerciseSet entity) {
        return repository.findAllByWorkoutExerciseIdAndIdNotOrderByOrder(
                entity.getWorkoutExercise().getId(),
                entity.getId()
        );
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
