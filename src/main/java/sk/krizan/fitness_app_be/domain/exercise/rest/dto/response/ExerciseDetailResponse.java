package sk.krizan.fitness_app_be.domain.exercise.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.response.ExerciseMuscleRoleResponse;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record ExerciseDetailResponse(

        Long id,

        String title,

        String thumbnailUrl,

        ReferenceDataResponse exerciseCategory,

        List<ExerciseMuscleRoleResponse> muscles,

        List<ReferenceDataResponse> movementPatterns,

        List<EquipmentResponse> requiredEquipment

) {
}
