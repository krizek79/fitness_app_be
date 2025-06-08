package sk.krizan.fitness_app_be.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record WorkoutExerciseSetCreateRequest(
        @NotNull
        Long workoutExerciseId,
        @Min(1)
        @NotNull
        Integer order,
        String workoutExerciseSetTypeKey,
        @Min(1)
        Integer goalRepetitions,
        @DecimalMin("0.125")
        Double goalWeight,
        @Schema(example = "PT1M30S")
        String goalTime,
        @Schema(example = "PT1M30S")
        String restDuration,
        @Length(max = 1024)
        String note
) {
}
