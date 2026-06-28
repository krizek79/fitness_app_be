package sk.krizan.fitness_app_be.domain.exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseDetailResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.exercise.mapper.ExerciseMapper;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseSimpleResponse;
import sk.krizan.fitness_app_be.domain.exercise.service.api.ExerciseService;

@RestController
@RequiredArgsConstructor
public class ExerciseController implements sk.krizan.fitness_app_be.domain.exercise.rest.api.ExerciseController {

    private final ExerciseService exerciseService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public PageResponse<ExerciseSimpleResponse> filterExercises(ExerciseFilterRequest request) {
        return exerciseService.filterExercises(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public ExerciseDetailResponse getExerciseById(Long id) {
        return ExerciseMapper.entityToDetailResponse(exerciseService.getExerciseById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public ExerciseDetailResponse createExercise(ExerciseInputRequest request, MultipartFile thumbnail) {
        return ExerciseMapper.entityToDetailResponse(exerciseService.createUpdateExercise(null, request, thumbnail));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public ExerciseDetailResponse updateExercise(Long id, ExerciseInputRequest request, MultipartFile thumbnail) {
        return ExerciseMapper.entityToDetailResponse(exerciseService.createUpdateExercise(id, request, thumbnail));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public void deleteThumbnail(Long id) {
        exerciseService.deleteThumbnail(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public void deleteExercise(Long id) {
        exerciseService.deleteExercise(id);
    }

}
