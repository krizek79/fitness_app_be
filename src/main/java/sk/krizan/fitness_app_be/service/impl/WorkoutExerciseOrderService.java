package sk.krizan.fitness_app_be.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.OrderableEntity;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.service.api.OrderableEntityOrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseOrderService implements OrderableEntityOrderService {

    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Override
    public void reorder(@NotNull EntityReorderEvent event) {
        WorkoutExercise modifiedWorkoutExercise = (WorkoutExercise) event.getEntity();
        if (modifiedWorkoutExercise.getWorkout() == null) {
            throw new IllegalOperationException("Workout is null.");
        }

        switch (event.getEntityLifeCycleEventEnum()) {
            case CREATE -> handleCreate(modifiedWorkoutExercise);
            case UPDATE -> handleUpdate(modifiedWorkoutExercise, event.getOriginalOrder());
            case DELETE -> handleDelete(modifiedWorkoutExercise);
        }
    }

    private void handleCreate(WorkoutExercise newWorkoutExercise) {
        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAllByWorkoutIdAndIdNotOrderByOrder(newWorkoutExercise.getWorkout().getId(), newWorkoutExercise.getId());
        int desiredOrder = newWorkoutExercise.getOrder() != null ? newWorkoutExercise.getOrder() : workoutExerciseList.size() + 1;

        if (desiredOrder > workoutExerciseList.size() + 1) {
            desiredOrder = workoutExerciseList.size() + 1;
            newWorkoutExercise.setOrder(desiredOrder);
        }

        for (WorkoutExercise workoutExercise : workoutExerciseList) {
            if (workoutExercise.getOrder() >= desiredOrder) {
                workoutExercise.setOrder(workoutExercise.getOrder() + 1);
            }
        }

        workoutExerciseRepository.saveAll(workoutExerciseList);
    }

    private void handleUpdate(WorkoutExercise updatedWorkoutExercise, int originalOrder) {
        if (updatedWorkoutExercise.getOrder() == null) {
            throw new IllegalOperationException("Updated workout exercise order cannot be null.");
        }

        int newOrder = updatedWorkoutExercise.getOrder();
        if (originalOrder == newOrder) {
            return;
        }

        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAllByWorkoutIdOrderByOrder(updatedWorkoutExercise.getWorkout().getId());

        if (newOrder < originalOrder) {
            for (WorkoutExercise workoutExercise : workoutExerciseList) {
                if (!workoutExercise.getId().equals(updatedWorkoutExercise.getId())
                        && workoutExercise.getOrder() >= newOrder
                        && workoutExercise.getOrder() < originalOrder) {
                    workoutExercise.setOrder(workoutExercise.getOrder() + 1);
                }
            }
        } else {
            for (WorkoutExercise workoutExercise : workoutExerciseList) {
                if (!workoutExercise.getId().equals(updatedWorkoutExercise.getId())
                        && workoutExercise.getOrder() <= newOrder
                        && workoutExercise.getOrder() > originalOrder) {
                    workoutExercise.setOrder(workoutExercise.getOrder() - 1);
                }
            }
        }

        workoutExerciseRepository.saveAll(workoutExerciseList);
    }

    private void handleDelete(WorkoutExercise deletedWorkoutExercise) {
        Integer deletedOrder = deletedWorkoutExercise.getOrder();
        if (deletedOrder == null) {
            return;
        }

        List<WorkoutExercise> workoutExerciseList = workoutExerciseRepository.findAllByWorkoutIdAndIdNotOrderByOrder(deletedWorkoutExercise.getWorkout().getId(), deletedWorkoutExercise.getId());
        for (WorkoutExercise workoutExercise : workoutExerciseList) {
            if (workoutExercise.getOrder() > deletedOrder) {
                workoutExercise.setOrder(workoutExercise.getOrder() - 1);
            }
        }

        workoutExerciseRepository.saveAll(workoutExerciseList);
    }

    @Override
    public Class<? extends OrderableEntity> supportedClass() {
        return WorkoutExercise.class;
    }
}
