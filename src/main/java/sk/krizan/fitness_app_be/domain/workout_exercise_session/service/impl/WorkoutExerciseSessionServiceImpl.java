package sk.krizan.fitness_app_be.domain.workout_exercise_session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.service.api.WorkoutExerciseService;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.mapper.WorkoutExerciseSessionMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.repository.WorkoutExerciseSessionRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.service.api.WorkoutExerciseSessionService;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request.WorkoutExerciseSetResultInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.service.api.WorkoutExerciseSetResultService;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseSessionServiceImpl implements WorkoutExerciseSessionService {

    private final WorkoutExerciseService workoutExerciseService;
    private final WorkoutExerciseSetResultService workoutExerciseSetResultService;

    private final WorkoutExerciseSessionRepository workoutExerciseSessionRepository;

    @Override
    public WorkoutExerciseSession getWorkoutExerciseSessionById(Long id) {
        return workoutExerciseSessionRepository.getByIdOrThrow(id);
    }

    @Override
    public void createUpdateWorkoutExerciseSession(WorkoutSession workoutSession, WorkoutExerciseSessionInputRequest request) {
        WorkoutExercise workoutExercise = workoutExerciseService.getWorkoutExerciseById(request.workoutExerciseId());

        WorkoutExerciseSession workoutExerciseSession;
        if (request.id() != null) {
            workoutExerciseSession = workoutSession.getWorkoutExerciseSessions().stream()
                    .filter(s -> s.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                            "WorkoutExerciseSession with id %d not found in WorkoutSession with id %d.".formatted(request.id(), workoutSession.getId())));
            WorkoutExerciseSessionMapper.inputRequestToEntity(workoutExerciseSession, request, workoutSession);
        } else {
            workoutExerciseSession = WorkoutExerciseSessionMapper.inputRequestToEntity(null, request, workoutSession);
        }

        workoutExerciseSession.setWorkoutExercise(workoutExercise);
        resolveWorkoutExerciseSetResults(workoutExerciseSession, request.workoutExerciseSetResults());
    }

    private void resolveWorkoutExerciseSetResults(WorkoutExerciseSession workoutExerciseSession, List<WorkoutExerciseSetResultInputRequest> results) {
        Set<Long> incomingIds = results.stream()
                .map(WorkoutExerciseSetResultInputRequest::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        workoutExerciseSession.getWorkoutExerciseSetResults().removeIf(
                r -> r.getId() != null && !incomingIds.contains(r.getId()));

        for (WorkoutExerciseSetResultInputRequest resultRequest : results) {
            workoutExerciseSetResultService.createUpdateWorkoutExerciseSetResult(workoutExerciseSession, resultRequest);
        }

        workoutExerciseSession.getWorkoutExerciseSetResults().sort(
                Comparator.comparing(r -> r.getOrder() != null ? r.getOrder() : Integer.MAX_VALUE));

        int currentOrder = 1;
        for (var result : workoutExerciseSession.getWorkoutExerciseSetResults()) {
            result.setOrder(currentOrder++);
        }
    }

}
