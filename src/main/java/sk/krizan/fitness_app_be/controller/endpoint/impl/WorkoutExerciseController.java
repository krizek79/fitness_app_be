package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;

@RestController
@RequiredArgsConstructor
public class WorkoutExerciseController implements sk.krizan.fitness_app_be.controller.endpoint.api.WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(WorkoutExerciseFilterRequest request) {
        return workoutExerciseService.filterWorkoutExercises(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseResponse getWorkoutExerciseById(Long id) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.getWorkoutExerciseById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseResponse createWorkoutExercise(WorkoutExerciseCreateRequest request) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.createWorkoutExercise(request));
    }

    @Deprecated
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseResponse updateWorkoutExercise(WorkoutExerciseUpdateRequest request) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.updateWorkoutExercise(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public SimpleListResponse<WorkoutExerciseResponse> batchUpdateWorkoutExercises(BatchUpdateRequest<WorkoutExerciseUpdateRequest> request) {
        return WorkoutExerciseMapper.entityListToSimpleListResponse(workoutExerciseService.batchUpdateWorkoutExercises(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public Long deleteWorkoutExercise(Long id) {
        return workoutExerciseService.deleteWorkoutExercise(id);
    }
}
