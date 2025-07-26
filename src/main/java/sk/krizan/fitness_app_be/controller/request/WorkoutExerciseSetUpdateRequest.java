package sk.krizan.fitness_app_be.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;

@Builder
public record WorkoutExerciseSetUpdateRequest(
        @NotNull
        Long id,
        @Min(1)
        @NotNull
        Integer order,
        WorkoutExerciseSetType workoutExerciseSetType,
        @Min(1)
        Integer goalRepetitions,
        @Min(1)
        Integer actualRepetitions,
        @DecimalMin("0.125")
        Double goalWeight,
        @DecimalMin("0.125")
        Double actualWeight,
        @Schema(example = "PT1M30S")
        String goalTime,
        @Schema(example = "PT1M30S")
        String actualTime,
        @Schema(example = "PT1M30S")
        String restDuration,
        @Length(max = 1024)
        String note
) {
}
