package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WorkoutExerciseCreateRequest(
    @NotNull
    Long workoutId,
    @NotNull
    Long exerciseId,
    @NotNull
    @Min(1)
    Integer order,
    @NotNull
    String workoutExerciseTypeKey
) {
}
