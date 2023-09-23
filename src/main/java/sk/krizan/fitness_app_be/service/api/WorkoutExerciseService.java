package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.CreateWorkoutExerciseRequest;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

public interface WorkoutExerciseService {

    WorkoutExercise getWorkoutExerciseById(Long id);
    WorkoutExercise createWorkoutExcercise(CreateWorkoutExerciseRequest request);
    Long deleteWorkoutExercise(Long id);
}
