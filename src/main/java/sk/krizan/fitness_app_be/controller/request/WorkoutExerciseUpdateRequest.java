package sk.krizan.fitness_app_be.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WorkoutExerciseUpdateRequest(
        @NotNull
        Long id,
        @NotNull
        @Min(1)
        @Schema(example = "5")
        Integer sets,
        @NotNull
        @Min(1)
        @Schema(example = "8")
        Integer repetitions,
        @Schema(example = "PT1M30S")
        String restDuration,
        @Min(1)
        @NotNull
        Integer order
) {
}
