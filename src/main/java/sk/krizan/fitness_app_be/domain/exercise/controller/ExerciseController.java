package sk.krizan.fitness_app_be.domain.exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.exercise.mapper.ExerciseMapper;
import sk.krizan.fitness_app_be.domain.exercise.service.api.ExerciseService;

@RestController
@RequiredArgsConstructor
public class ExerciseController implements sk.krizan.fitness_app_be.domain.exercise.rest.api.ExerciseController {

    private final ExerciseService exerciseService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public PageResponse<ExerciseResponse> filterExercises(ExerciseFilterRequest request) {
        return exerciseService.filterExercises(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public ExerciseResponse getExerciseById(Long id) {
        return ExerciseMapper.entityToResponse(exerciseService.getExerciseById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public ExerciseResponse createExercise(ExerciseCreateRequest request) {
        return ExerciseMapper.entityToResponse(exerciseService.createExercise(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public void deleteExercise(Long id) {
        exerciseService.deleteExercise(id);
    }

}
