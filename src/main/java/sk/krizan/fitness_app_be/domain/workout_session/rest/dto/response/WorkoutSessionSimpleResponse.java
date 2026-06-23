package sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;

import java.time.LocalDateTime;

@Builder
@FieldNameConstants
public record WorkoutSessionSimpleResponse(

        Long id,

        WorkoutSimpleResponse workout,

        Long weekWorkoutId,

        WorkoutStatus status,

        LocalDateTime startedAt,

        LocalDateTime finishedAt

) {}
