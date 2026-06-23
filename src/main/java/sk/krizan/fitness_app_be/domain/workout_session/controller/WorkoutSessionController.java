package sk.krizan.fitness_app_be.domain.workout_session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.workout_session.mapper.WorkoutSessionMapper;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionDetailResponse;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout_session.service.api.WorkoutSessionService;

@RestController
@RequiredArgsConstructor
public class WorkoutSessionController implements sk.krizan.fitness_app_be.domain.workout_session.rest.api.WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    @Override
    public PageResponse<WorkoutSessionSimpleResponse> filterWorkoutSessions(WorkoutSessionFilterRequest request) {
        return workoutSessionService.filterWorkoutSessions(request);
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT_SESSION', 'READ')")
    public WorkoutSessionDetailResponse getWorkoutSessionById(Long id) {
        return WorkoutSessionMapper.entityToDetailResponse(workoutSessionService.getWorkoutSessionById(id));
    }

    @Override
    @PreAuthorize("hasPermission(#request.workoutId(), 'WORKOUT', 'READ')")
    public WorkoutSessionDetailResponse createWorkoutSession(WorkoutSessionInputRequest request) {
        return WorkoutSessionMapper.entityToDetailResponse(workoutSessionService.createWorkoutSession(request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT_SESSION', 'UPDATE')")
    public WorkoutSessionDetailResponse updateWorkoutSession(Long id, WorkoutSessionInputRequest request) {
        return WorkoutSessionMapper.entityToDetailResponse(workoutSessionService.updateWorkoutSession(id, request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT_SESSION', 'DELETE')")
    public void deleteWorkoutSession(Long id) {
        workoutSessionService.deleteWorkoutSession(id);
    }

}
