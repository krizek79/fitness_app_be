package sk.krizan.fitness_app_be.domain.workout_session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.service.api.WorkoutService;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.service.api.WorkoutExerciseSessionService;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.mapper.WorkoutSessionMapper;
import sk.krizan.fitness_app_be.domain.workout_session.repository.WorkoutSessionRepository;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout_session.service.api.WorkoutSessionService;
import sk.krizan.fitness_app_be.domain.workout_session.specification.WorkoutSessionSpecification;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final WorkoutExerciseSessionService workoutExerciseSessionService;

    private final WeekWorkoutRepository weekWorkoutRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    private static final List<String> supportedSortFields = List.of(
            WorkoutSession.Fields.id,
            WorkoutSession.Fields.startedAt,
            WorkoutSession.Fields.finishedAt,
            WorkoutSession.Fields.status
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WorkoutSessionSimpleResponse> filterWorkoutSessions(WorkoutSessionFilterRequest request) {
        User currentUser = userService.getOrCreateCurrentUser();
        boolean isUserAdmin = userService.isUserAdmin(currentUser);

        Specification<WorkoutSession> specification = WorkoutSessionSpecification.filter(request, currentUser, isUserAdmin);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<WorkoutSession> page = workoutSessionRepository.findAll(specification, pageable);
        List<WorkoutSessionSimpleResponse> responseList = page.stream()
                .map(WorkoutSessionMapper::entityToSimpleResponse)
                .collect(Collectors.toList());

        return PageResponse.<WorkoutSessionSimpleResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public WorkoutSession getWorkoutSessionById(Long id) {
        return workoutSessionRepository.getByIdOrThrow(id);
    }

    @Override
    @Transactional
    public WorkoutSession createWorkoutSession(WorkoutSessionInputRequest request) {
        Workout workout = workoutService.getWorkoutById(request.workoutId());
        WeekWorkout weekWorkout = request.weekWorkoutId() != null
                ? weekWorkoutRepository.getByIdOrThrow(request.weekWorkoutId())
                : null;

        WorkoutSession workoutSession = WorkoutSessionMapper.createFromRequest(request, workout, weekWorkout);
        resolveWorkoutExerciseSessions(workoutSession, request.workoutExerciseSessions());

        return workoutSessionRepository.save(workoutSession);
    }

    @Override
    @Transactional
    public WorkoutSession updateWorkoutSession(Long id, WorkoutSessionInputRequest request) {
        WorkoutSession workoutSession = getWorkoutSessionById(id);
        WorkoutSessionMapper.inputRequestToEntity(workoutSession, request);
        resolveWorkoutExerciseSessions(workoutSession, request.workoutExerciseSessions());

        return workoutSessionRepository.save(workoutSession);
    }

    @Override
    @Transactional
    public void deleteWorkoutSession(Long id) {
        WorkoutSession workoutSession = getWorkoutSessionById(id);
        workoutSessionRepository.delete(workoutSession);
    }

    private void resolveWorkoutExerciseSessions(WorkoutSession workoutSession, List<WorkoutExerciseSessionInputRequest> exerciseSessions) {
        Set<Long> incomingIds = exerciseSessions.stream()
                .map(WorkoutExerciseSessionInputRequest::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        workoutSession.getWorkoutExerciseSessions().removeIf(
                s -> s.getId() != null && !incomingIds.contains(s.getId()));

        for (WorkoutExerciseSessionInputRequest exerciseSessionRequest : exerciseSessions) {
            workoutExerciseSessionService.createUpdateWorkoutExerciseSession(workoutSession, exerciseSessionRequest);
        }

        workoutSession.getWorkoutExerciseSessions().sort(
                Comparator.comparing(s -> s.getOrder() != null ? s.getOrder() : Integer.MAX_VALUE));

        int currentOrder = 1;
        for (var exerciseSession : workoutSession.getWorkoutExerciseSessions()) {
            exerciseSession.setOrder(currentOrder++);
        }
    }

}
