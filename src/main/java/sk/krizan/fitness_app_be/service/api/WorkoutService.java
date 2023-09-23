package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.CreateWorkoutRequest;
import sk.krizan.fitness_app_be.model.entity.Workout;

public interface WorkoutService {

    Workout getWorkoutById(Long id);
    Workout createWorkout(CreateWorkoutRequest request);
    Long deleteWorkout(Long id);
}
