package sk.krizan.fitness_app_be.domain.exercise.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;

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
        String name,
        List<MuscleGroup> muscleGroupList
) {
}
