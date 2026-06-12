package sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExerciseMetric;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;

import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutExerciseInputRequest(
        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Long id,

        @NotNull
        Long exerciseId,

        @NotNull
        @Min(1)
        Integer order,

        @NotNull
        WorkoutExerciseMetric workoutExerciseMetric,

        @Length(max = 1024)
        String note,

        @Valid
        @NotNull
        List<WorkoutExerciseSetInputRequest> workoutExerciseSets
) {}
