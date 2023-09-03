package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.model.entity.Exercise;

public interface ExerciseService {

    Exercise getExerciseById(Long id);
    Long deleteExercise(Long id);
}
