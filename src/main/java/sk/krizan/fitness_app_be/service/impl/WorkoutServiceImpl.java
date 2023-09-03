package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.WorkoutService;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Override
    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Workout with id { " + id + " } does not exist."));
    }

    @Override
    public Long deleteWorkout(Long id) {
        Workout workout = getWorkoutById(id);
        workoutRepository.delete(workout);
        return workout.getId();
    }
}
