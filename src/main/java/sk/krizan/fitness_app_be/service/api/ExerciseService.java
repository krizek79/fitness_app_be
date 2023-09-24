package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.CreateExerciseRequest;
import sk.krizan.fitness_app_be.model.entity.Exercise;

public interface ExerciseService {

    Exercise getExerciseById(Long id);
    Exercise createExercise(CreateExerciseRequest request);
    Long deleteExercise(Long id);
}
