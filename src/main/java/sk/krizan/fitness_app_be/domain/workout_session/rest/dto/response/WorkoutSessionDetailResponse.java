package sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.response.WorkoutExerciseSessionResponse;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutSessionDetailResponse(

        Long id,

        WorkoutSimpleResponse workout,

        Long weekWorkoutId,

        WorkoutStatus status,

        LocalDateTime startedAt,

        LocalDateTime finishedAt,

        List<WorkoutExerciseSessionResponse> workoutExerciseSessions

) {}
