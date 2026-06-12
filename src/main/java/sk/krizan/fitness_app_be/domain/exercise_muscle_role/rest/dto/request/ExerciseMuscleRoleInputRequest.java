package sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRoleType;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;

@Builder
@FieldNameConstants
public record ExerciseMuscleRoleInputRequest(

        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Long id,

        @NotNull
        Muscle muscle,

        @NotNull
        ExerciseMuscleRoleType type

) {
}
