package sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;

import java.math.BigDecimal;

@Builder
@FieldNameConstants
public record WorkoutExerciseSetCreateRequest(
        @NotNull
        Long workoutExerciseId,
        @Min(1)
        @NotNull
        Integer order,
        WorkoutExerciseSetType workoutExerciseSetType,
        @Min(1)
        Integer goalRepetitions,
        @DecimalMin("0.125")
        BigDecimal goalWeight,
        @Schema(example = "PT1M30S")
        String goalTime,
        @Schema(example = "PT1M30S")
        String restDuration,
        @Length(max = 1024)
        String note
) {
}
