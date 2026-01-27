package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.exercise.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.exercise.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;

public interface ExerciseService {

    PageResponse<ExerciseResponse> filterExercises(ExerciseFilterRequest request);

    Exercise getExerciseById(Long id);

    Exercise createExercise(ExerciseCreateRequest request);

    Long deleteExercise(Long id);
}
