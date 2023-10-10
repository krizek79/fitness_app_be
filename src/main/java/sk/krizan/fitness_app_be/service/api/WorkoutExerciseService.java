package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

public interface WorkoutExerciseService {

    WorkoutExercise getWorkoutExerciseById(Long id);
    WorkoutExercise createWorkoutExercise(WorkoutExerciseCreateRequest request);
    WorkoutExercise updateWorkoutExercise(Long id, WorkoutExerciseUpdateRequest request);
    Long deleteWorkoutExercise(Long id);
}
