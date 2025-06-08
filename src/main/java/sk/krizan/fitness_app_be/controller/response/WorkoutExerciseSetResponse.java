package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record WorkoutExerciseSetResponse(
        Long id,
        Long workoutExerciseId,
        Integer order,
        EnumResponse workoutExerciseSetTypeResponse,
        Integer goalRepetitions,
        Integer actualRepetitions,
        Double goalWeight,
        Double actualWeight,
        String goalTime,
        String actualTime,
        String restDuration,
        Boolean completed,
        String note
) {
}
