package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;

import java.util.Set;

@Builder
public record ExerciseCreateRequest(
    @NotEmpty
    @Size(min = 2, max = 64)
    String name,
    @NotNull
    Set<MuscleGroup> muscleGroupSet
) {
}
