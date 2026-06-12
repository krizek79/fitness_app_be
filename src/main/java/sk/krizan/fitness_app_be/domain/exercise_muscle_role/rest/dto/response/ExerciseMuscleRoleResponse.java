package sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

@Builder
@FieldNameConstants
public record ExerciseMuscleRoleResponse(

        Long id,

        Long exerciseId,

        ReferenceDataResponse muscle,

        ReferenceDataResponse type

) {
}
