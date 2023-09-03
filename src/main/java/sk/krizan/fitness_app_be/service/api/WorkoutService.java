package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.model.entity.Workout;

public interface WorkoutService {

    Workout getWorkoutById(Long id);
    Long deleteWorkout(Long id);
}
