package sk.krizan.fitness_app_be.domain.exercise.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.exercise.entity.ExerciseCategory;
import sk.krizan.fitness_app_be.domain.exercise.entity.MovementPattern;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;

import java.util.List;

@Builder
@FieldNameConstants
public record ExerciseFilterRequest(
        @NotNull
        @Min(0)
        Integer page,
        @NotNull
        @Min(1)
        Integer size,
        @NotNull
        String sortBy,
        @NotNull
        @Pattern(regexp = "ASC|DESC|asc|desc")
        String sortDirection,
        String title,
        ExerciseCategory exerciseCategory,
        List<MovementPattern> movementPatterns,
        List<Muscle> muscles,
        List<Long> requiredEquipmentIds
) {
}
