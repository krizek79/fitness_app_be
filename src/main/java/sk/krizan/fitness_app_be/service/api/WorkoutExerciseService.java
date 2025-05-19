package sk.krizan.fitness_app_be.service.api;

import jakarta.validation.Valid;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

public interface WorkoutExerciseService {

    PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(@Valid WorkoutExerciseFilterRequest request);
    WorkoutExercise getWorkoutExerciseById(Long id);
    WorkoutExercise createWorkoutExercise(WorkoutExerciseCreateRequest request);
    WorkoutExercise updateWorkoutExercise(WorkoutExerciseUpdateRequest request);
    Long deleteWorkoutExercise(Long id);
}
