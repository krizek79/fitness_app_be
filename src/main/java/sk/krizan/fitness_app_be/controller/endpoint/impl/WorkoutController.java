package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.mapper.WorkoutMapper;
import sk.krizan.fitness_app_be.service.api.WorkoutService;

@RestController
@RequiredArgsConstructor
public class WorkoutController implements sk.krizan.fitness_app_be.controller.endpoint.api.WorkoutController {

    private final WorkoutService workoutService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<WorkoutResponse> filterWorkouts(WorkoutFilterRequest request) {
        return workoutService.filterWorkouts(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutResponse getWorkoutById(Long id) {
        return WorkoutMapper.entityToResponse(workoutService.getWorkoutById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutResponse createWorkout(WorkoutCreateRequest request) {
        return WorkoutMapper.entityToResponse(workoutService.createWorkout(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutResponse updateWorkout(
            Long id, WorkoutUpdateRequest request) {
        return WorkoutMapper.entityToResponse(workoutService.updateWorkout(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public Long deleteWorkout(Long id) {
        return workoutService.deleteWorkout(id);
    }
}
