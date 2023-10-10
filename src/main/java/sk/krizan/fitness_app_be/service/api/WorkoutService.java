package sk.krizan.fitness_app_be.service.api;

import java.util.List;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Workout;

public interface WorkoutService {

    PageResponse<WorkoutResponse> filterWorkouts(WorkoutFilterRequest request);
    Workout getWorkoutById(Long id);
    Workout createWorkout(WorkoutCreateRequest request);
    Workout updateWorkoutLevel(Long id, List<String> tagNames);
    Workout updateWorkoutLevel(Long id, String levelKey);
    Long deleteWorkout(Long id);
}
