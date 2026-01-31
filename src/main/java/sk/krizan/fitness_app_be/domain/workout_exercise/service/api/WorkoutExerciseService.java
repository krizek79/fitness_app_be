package sk.krizan.fitness_app_be.domain.workout_exercise.service.api;

import jakarta.validation.Valid;
import sk.krizan.fitness_app_be.common.rest.dto.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;

import java.util.List;

public interface WorkoutExerciseService {

    PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(@Valid WorkoutExerciseFilterRequest request);

    WorkoutExercise getWorkoutExerciseById(Long id);

    WorkoutExercise createWorkoutExercise(WorkoutExerciseCreateRequest request);

    WorkoutExercise updateWorkoutExercise(WorkoutExerciseUpdateRequest request);

    List<WorkoutExercise> batchUpdateWorkoutExercises(BatchUpdateRequest<WorkoutExerciseUpdateRequest> request);

    Long deleteWorkoutExercise(Long id);
}
