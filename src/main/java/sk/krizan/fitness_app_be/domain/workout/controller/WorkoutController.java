package sk.krizan.fitness_app_be.domain.workout.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.workout.mapper.WorkoutMapper;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout.service.api.WorkoutService;

@RestController
@RequiredArgsConstructor
public class WorkoutController implements sk.krizan.fitness_app_be.domain.workout.rest.api.WorkoutController {

    private final WorkoutService workoutService;

    @Override
    public PageResponse<WorkoutSimpleResponse> filterWorkouts(WorkoutFilterRequest request) {
        return workoutService.filterWorkouts(request);
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT', 'READ')")
    public WorkoutDetailResponse getWorkoutById(Long id) {
        return WorkoutMapper.entityToDetailResponse(workoutService.getWorkoutById(id));
    }

    @Override
    public WorkoutDetailResponse createWorkout(WorkoutInputRequest request) {
        return WorkoutMapper.entityToDetailResponse(workoutService.createUpdateWorkout(null, request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT', 'UPDATE')")
    public WorkoutDetailResponse updateWorkout(Long id, WorkoutInputRequest request) {
        return WorkoutMapper.entityToDetailResponse(workoutService.createUpdateWorkout(id, request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT', 'DELETE')")
    public void deleteWorkout(Long id) {
        workoutService.deleteWorkout(id);
    }

}
