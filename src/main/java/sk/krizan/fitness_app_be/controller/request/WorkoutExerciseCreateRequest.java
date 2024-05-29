package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record WorkoutExerciseCreateRequest(
    @NotEmpty
    Long workoutId,
    @NotEmpty
    Long exerciseId,
    @NotEmpty
    @Size(
        min = 1,
        message = "Number of sets cannot be less than 1."
    )
    Integer sets,
    @NotEmpty
    @Size(
        min = 1,
        message = "Number of repetitions cannot be less than 1."
    )
    Integer repetitions
) {
}
