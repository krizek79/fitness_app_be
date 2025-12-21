package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WorkoutExerciseSetResponse(
        Long id,
        Long workoutExerciseId,
        Integer order,
        EnumResponse workoutExerciseSetTypeResponse,
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
