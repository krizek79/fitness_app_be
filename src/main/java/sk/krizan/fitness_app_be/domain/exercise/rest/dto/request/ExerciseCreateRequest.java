package sk.krizan.fitness_app_be.domain.exercise.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;

import java.util.Set;

@Builder
@FieldNameConstants
public record ExerciseCreateRequest(
    @NotEmpty
    @Size(min = 2, max = 64)
    String name,
    @NotNull
    Set<MuscleGroup> muscleGroupSet
) {
}
