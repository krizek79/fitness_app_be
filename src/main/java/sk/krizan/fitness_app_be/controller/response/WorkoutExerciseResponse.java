package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record WorkoutExerciseResponse(
        Long id,
        Long workoutId,
        Integer order,
        String exerciseName,
        EnumResponse workoutExerciseTypeResponse,
        String note
) {
}
