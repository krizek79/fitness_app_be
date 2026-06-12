package sk.krizan.fitness_app_be.domain.exercise.service.api;

import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseSimpleResponse;

public interface ExerciseService {

    PageResponse<ExerciseSimpleResponse> filterExercises(ExerciseFilterRequest request);

    Exercise getExerciseById(Long id);

    Exercise createUpdateExercise(Long id, ExerciseInputRequest request, MultipartFile thumbnail);

    void deleteExercise(Long id);

}
