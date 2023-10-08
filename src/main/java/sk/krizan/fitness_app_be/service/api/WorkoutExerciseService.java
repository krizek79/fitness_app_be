package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

public interface WorkoutExerciseService {

    WorkoutExercise getWorkoutExerciseById(Long id);
    WorkoutExercise createWorkoutExcercise(WorkoutExerciseCreateRequest request);
    Long deleteWorkoutExercise(Long id);
}
