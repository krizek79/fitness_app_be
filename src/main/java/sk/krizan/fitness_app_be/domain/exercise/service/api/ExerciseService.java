package sk.krizan.fitness_app_be.domain.exercise.service.api;

import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;

public interface ExerciseService {

    PageResponse<ExerciseResponse> filterExercises(ExerciseFilterRequest request);

    Exercise getExerciseById(Long id);

    Exercise createExercise(ExerciseCreateRequest request);

    void deleteExercise(Long id);
}
