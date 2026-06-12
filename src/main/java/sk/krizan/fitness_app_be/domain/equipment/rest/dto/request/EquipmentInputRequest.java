package sk.krizan.fitness_app_be.domain.equipment.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record EquipmentInputRequest(
        @NotEmpty
        String title
) {
}
