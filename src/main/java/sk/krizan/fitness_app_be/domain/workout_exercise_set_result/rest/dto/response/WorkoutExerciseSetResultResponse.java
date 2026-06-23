package sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

import java.math.BigDecimal;

@Builder
@FieldNameConstants
public record WorkoutExerciseSetResultResponse(

        Long id,

        Long workoutExerciseSessionId,

        Long workoutExerciseSetId,

        Integer order,

        ReferenceDataResponse workoutExerciseSetType,

        Integer repetitions,

        BigDecimal weight,

        Long timeSeconds,

        BigDecimal distanceMeters,

        Long restDurationSeconds,

        Boolean completed,

        String note

) {}
