package sk.krizan.fitness_app_be.domain.workout.service.api;

import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutResponse;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;

public interface WorkoutService {

    PageResponse<WorkoutResponse> filterWorkouts(WorkoutFilterRequest request);

    Workout getWorkoutById(Long id);

    Workout createWorkout(WorkoutCreateRequest request);

    Workout updateWorkout(Long id, WorkoutUpdateRequest request);

    Long deleteWorkout(Long id);
}
