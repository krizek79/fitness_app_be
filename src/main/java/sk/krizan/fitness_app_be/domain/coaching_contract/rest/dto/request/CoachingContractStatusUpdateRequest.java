package sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractAction;

@Builder
@FieldNameConstants
public record CoachingContractStatusUpdateRequest(
        @NotNull
        CoachingContractAction action
) {
}
