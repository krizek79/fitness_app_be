package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WorkoutExerciseUpdateRequest(
        @NotNull
        Long id,
        @Min(1)
        @NotNull
        Integer order,
        @NotNull
        String workoutExerciseTypeKey
) {
}
