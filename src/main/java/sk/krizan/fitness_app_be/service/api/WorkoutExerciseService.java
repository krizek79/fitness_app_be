package sk.krizan.fitness_app_be.service.api;

import jakarta.validation.Valid;
import sk.krizan.fitness_app_be.controller.request.common.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.workout_exercise.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.workout_exercise.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.workout_exercise.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.util.List;

public interface WorkoutExerciseService {

    PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(@Valid WorkoutExerciseFilterRequest request);

    WorkoutExercise getWorkoutExerciseById(Long id);

    WorkoutExercise createWorkoutExercise(WorkoutExerciseCreateRequest request);

    WorkoutExercise updateWorkoutExercise(WorkoutExerciseUpdateRequest request);

    List<WorkoutExercise> batchUpdateWorkoutExercises(BatchUpdateRequest<WorkoutExerciseUpdateRequest> request);

    Long deleteWorkoutExercise(Long id);
}
