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
        ReferenceDataResponse workoutExerciseSetTypeResponse,
        Integer goalRepetitions,
        Integer actualRepetitions,
        BigDecimal goalWeight,
        BigDecimal actualWeight,
        String goalTime,
        String actualTime,
        String restDuration,
        Boolean completed,
        String note
) {
}
