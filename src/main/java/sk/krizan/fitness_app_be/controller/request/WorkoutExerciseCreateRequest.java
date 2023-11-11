package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record WorkoutExerciseCreateRequest(
    @NotEmpty(message = "WorkoutId is mandatory.")
    Long workoutId,
    @NotEmpty(message = "ExerciseId is mandatory.")
    Long exerciseId,
    @NotEmpty(message = "Number of sets is mandatory.")
    @Size(
        min = 1,
        message = "Number of sets cannot be less than 1."
    )
    Integer sets,
    @NotEmpty(message = "Number of repetitions is mandatory.")
    @Size(
        min = 1,
        message = "Number of repetitions cannot be less than 1."
    )
    Integer repetitions
) {
}
