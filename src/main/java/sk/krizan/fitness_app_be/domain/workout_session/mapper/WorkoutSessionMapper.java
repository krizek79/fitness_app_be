package sk.krizan.fitness_app_be.domain.workout_session.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.mapper.WorkoutMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.mapper.WorkoutExerciseSessionMapper;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionDetailResponse;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionSimpleResponse;

import java.util.Comparator;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutSessionMapper {

    public static WorkoutSessionSimpleResponse entityToSimpleResponse(WorkoutSession workoutSession) {
        return WorkoutSessionSimpleResponse.builder()
                .id(workoutSession.getId())
                .workout(WorkoutMapper.entityToSimpleResponse(workoutSession.getWorkout()))
                .weekWorkoutId(workoutSession.getWeekWorkout() != null ? workoutSession.getWeekWorkout().getId() : null)
                .status(workoutSession.getStatus())
                .startedAt(workoutSession.getStartedAt())
                .finishedAt(workoutSession.getFinishedAt())
                .build();
    }

    public static WorkoutSessionDetailResponse entityToDetailResponse(WorkoutSession workoutSession) {
        return WorkoutSessionDetailResponse.builder()
                .id(workoutSession.getId())
                .workout(WorkoutMapper.entityToSimpleResponse(workoutSession.getWorkout()))
                .weekWorkoutId(workoutSession.getWeekWorkout() != null ? workoutSession.getWeekWorkout().getId() : null)
                .status(workoutSession.getStatus())
                .startedAt(workoutSession.getStartedAt())
                .finishedAt(workoutSession.getFinishedAt())
                .workoutExerciseSessions(
                        workoutSession.getWorkoutExerciseSessions().stream()
                                .sorted(Comparator.comparingInt(WorkoutExerciseSession::getOrder))
                                .map(WorkoutExerciseSessionMapper::entityToResponse)
                                .toList())
                .build();
    }

    public static void inputRequestToEntity(WorkoutSession workoutSession, WorkoutSessionInputRequest request) {
        if (request.status() != null) {
            workoutSession.setStatus(request.status());
        }
        workoutSession.setStartedAt(request.startedAt());
        workoutSession.setFinishedAt(request.finishedAt());
    }

    public static WorkoutSession createFromRequest(WorkoutSessionInputRequest request, Workout workout, WeekWorkout weekWorkout) {
        WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setWorkout(workout);
        workoutSession.setWeekWorkout(weekWorkout);
        inputRequestToEntity(workoutSession, request);
        return workoutSession;
    }

}
