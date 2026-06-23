package sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

import java.math.BigDecimal;

@Builder
@FieldNameConstants
public record WorkoutExerciseSetResponse(

        Long id,

        Long workoutExerciseId,

        Integer order,

        ReferenceDataResponse workoutExerciseSetType,

        Integer goalRepetitions,

        BigDecimal goalWeight,

        Long goalTimeSeconds,

        BigDecimal goalDistanceMeters,

        Long restDurationSeconds,

        String note

) {
}
