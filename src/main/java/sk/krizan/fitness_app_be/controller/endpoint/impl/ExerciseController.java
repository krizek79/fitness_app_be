package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.mapper.ExerciseMapper;
import sk.krizan.fitness_app_be.service.api.ExerciseService;

@RestController
@RequiredArgsConstructor
public class ExerciseController implements sk.krizan.fitness_app_be.controller.endpoint.api.ExerciseController {

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
    public Long deleteExercise(Long id) {
        return exerciseService.deleteExercise(id);
    }
}
