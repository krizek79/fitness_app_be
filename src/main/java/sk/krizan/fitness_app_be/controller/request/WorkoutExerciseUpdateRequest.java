package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseType;

@Builder
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
