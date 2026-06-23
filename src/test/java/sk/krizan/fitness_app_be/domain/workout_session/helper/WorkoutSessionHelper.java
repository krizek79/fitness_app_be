package sk.krizan.fitness_app_be.domain.workout_session.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionDetailResponse;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionSimpleResponse;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkoutSessionHelper {

    public static WorkoutSession createWorkoutSession(Workout workout, WeekWorkout weekWorkout) {
        WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setWorkout(workout);
        workoutSession.setWeekWorkout(weekWorkout);
        workoutSession.setStatus(WorkoutStatus.NOT_STARTED);
        return workoutSession;
    }

    public static WorkoutSessionInputRequest createInputRequest(
            Long workoutId,
            Long weekWorkoutId,
            WorkoutStatus status,
            List<WorkoutExerciseSessionInputRequest> workoutExerciseSessions
    ) {
        return WorkoutSessionInputRequest.builder()
                .workoutId(workoutId)
                .weekWorkoutId(weekWorkoutId)
                .status(status)
                .workoutExerciseSessions(workoutExerciseSessions)
                .build();
    }

    public static WorkoutSessionFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            Long workoutId,
            Long weekWorkoutId,
            Long profileId,
            WorkoutStatus status
    ) {
        return WorkoutSessionFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .workoutId(workoutId)
                .weekWorkoutId(weekWorkoutId)
                .profileId(profileId)
                .status(status)
                .build();
    }

    public static void assertWorkoutSessionDetailResponse(WorkoutSession workoutSession, WorkoutSessionDetailResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutSession.getId(), response.id());
        Assertions.assertEquals(workoutSession.getStatus(), response.status());
        Assertions.assertEquals(workoutSession.getStartedAt(), response.startedAt());
        Assertions.assertEquals(workoutSession.getFinishedAt(), response.finishedAt());
        if (workoutSession.getWeekWorkout() != null) {
            Assertions.assertEquals(workoutSession.getWeekWorkout().getId(), response.weekWorkoutId());
        } else {
            Assertions.assertNull(response.weekWorkoutId());
        }
        WorkoutHelper.assertWorkoutSimpleResponse(workoutSession.getWorkout(), response.workout());
    }

    public static void assertWorkoutSessionSimpleResponse(WorkoutSession workoutSession, WorkoutSessionSimpleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutSession.getId(), response.id());
        Assertions.assertEquals(workoutSession.getStatus(), response.status());
        WorkoutHelper.assertWorkoutSimpleResponse(workoutSession.getWorkout(), response.workout());
    }

    public static void assertInputToEntity(WorkoutSession workoutSession, WorkoutSessionInputRequest request) {
        Assertions.assertNotNull(workoutSession);
        Assertions.assertNotNull(workoutSession.getId());
        Assertions.assertEquals(request.workoutId(), workoutSession.getWorkout().getId());
        if (request.status() != null) {
            Assertions.assertEquals(request.status(), workoutSession.getStatus());
        } else {
            Assertions.assertEquals(WorkoutStatus.NOT_STARTED, workoutSession.getStatus());
        }
    }

}
