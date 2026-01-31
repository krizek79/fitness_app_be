package sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExerciseType;

@Builder
@FieldNameConstants
public record WorkoutExerciseUpdateRequest(
        @NotNull
        Long id,
        @Min(1)
        @NotNull
        Integer order,
        @NotNull
        WorkoutExerciseType workoutExerciseType,
        @Length(max = 1024)
        String note
) {
}
