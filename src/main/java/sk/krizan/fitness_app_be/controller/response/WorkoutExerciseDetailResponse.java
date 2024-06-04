package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record WorkoutExerciseDetailResponse(
    Long id,
    Long workoutId,
    String exerciseName,
    Integer sets,
    Integer repetitions,
    String restDuration
) {
}
