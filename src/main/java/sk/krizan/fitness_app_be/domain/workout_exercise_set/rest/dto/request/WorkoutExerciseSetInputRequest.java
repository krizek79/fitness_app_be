package sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;

import java.math.BigDecimal;

@Builder
@FieldNameConstants
public record WorkoutExerciseSetInputRequest(

        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Long id,

        @Min(1)
        @NotNull
        Integer order,

        @NotNull
        WorkoutExerciseSetType workoutExerciseSetType,

        @Min(1)
        Integer goalRepetitions,

        @Min(1)
        @Null(groups = CreateGroup.class)
        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Integer actualRepetitions,

        @DecimalMin("0.125")
        BigDecimal goalWeight,

        @DecimalMin("0.125")
        @Null(groups = CreateGroup.class)
        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        BigDecimal actualWeight,

        @PositiveOrZero
        Long goalTimeSeconds,

        @PositiveOrZero
        @Null(groups = CreateGroup.class)
        Long actualTimeSeconds,

        @PositiveOrZero
        Long restDurationSeconds,

        @Length(max = 1024)
        String note,

        @Null(groups = CreateGroup.class)
        Boolean completed
) {}
