package sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;

import java.math.BigDecimal;

@Builder
@FieldNameConstants
public record WorkoutExerciseSetResultInputRequest(

        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Long id,

        @Schema(description = "Optional reference to the planned WorkoutExerciseSet this result is based on.")
        Long workoutExerciseSetId,

        @Min(1)
        @NotNull
        Integer order,

        @NotNull
        WorkoutExerciseSetType workoutExerciseSetType,

        @PositiveOrZero
        Integer repetitions,

        @PositiveOrZero
        BigDecimal weight,

        @PositiveOrZero
        Long timeSeconds,

        @PositiveOrZero
        BigDecimal distanceMeters,

        @PositiveOrZero
        Long restDurationSeconds,

        Boolean completed,

        @Length(max = 1024)
        String note

) {}
