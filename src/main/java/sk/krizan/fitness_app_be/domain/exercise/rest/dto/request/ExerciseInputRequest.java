package sk.krizan.fitness_app_be.domain.exercise.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.exercise.entity.ExerciseCategory;
import sk.krizan.fitness_app_be.domain.exercise.entity.MovementPattern;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;

import java.util.List;
import java.util.Set;

@Builder
@FieldNameConstants
public record ExerciseInputRequest(

    @NotEmpty
    @Size(min = 2, max = 64)
    String title,

    @NotNull
    ExerciseCategory exerciseCategory,

    @NotNull
    Set<MovementPattern> movementPatterns,

    @NotNull
    List<Long> requiredEquipmentIds,

    @Valid
    @NotNull
    List<ExerciseMuscleRoleInputRequest> muscles

) {
}
