package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.model.entity.Workout;

public interface WorkoutService {

    PageResponse<WorkoutSimpleResponse> filterWorkouts(WorkoutFilterRequest request);
    Workout getWorkoutById(Long id);
    Workout createWorkout(WorkoutCreateRequest request);
    Workout updateWorkout(Long id, WorkoutUpdateRequest request);
    Long deleteWorkout(Long id);
}
