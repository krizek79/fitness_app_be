package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record WorkoutExerciseResponse(
    Long id,
    Long workoutId,
    Long exerciseId,
    Integer sets,
    Integer repetitions
) {
}
