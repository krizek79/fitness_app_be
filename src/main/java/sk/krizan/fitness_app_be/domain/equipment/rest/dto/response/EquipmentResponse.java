package sk.krizan.fitness_app_be.domain.equipment.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record EquipmentResponse(
        Long id,
        String title,
        String thumbnailUrl
) {
}
