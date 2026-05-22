package sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record CoachingContractCreateRequest(
        @NotNull
        Long coachId,
        @NotNull
        Long clientId
) {
}
