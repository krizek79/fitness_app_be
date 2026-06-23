package sk.krizan.fitness_app_be.domain.workout_session.service.api;

import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionSimpleResponse;

public interface WorkoutSessionService {

    PageResponse<WorkoutSessionSimpleResponse> filterWorkoutSessions(WorkoutSessionFilterRequest request);

    WorkoutSession getWorkoutSessionById(Long id);

    WorkoutSession createWorkoutSession(WorkoutSessionInputRequest request);

    WorkoutSession updateWorkoutSession(Long id, WorkoutSessionInputRequest request);

    void deleteWorkoutSession(Long id);

}
