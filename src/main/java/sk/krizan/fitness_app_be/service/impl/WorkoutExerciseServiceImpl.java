package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Override
    public WorkoutExercise getWorkoutExerciseById(Long id) {
        return workoutExerciseRepository.findById(id).orElseThrow(
            () -> new NotFoundException("WorkoutExercise with id { " + id + " } does not exist."));
    }

    @Override
    public Long deleteWorkoutExercise(Long id) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);
        workoutExerciseRepository.delete(workoutExercise);
        return workoutExercise.getId();
    }
}
