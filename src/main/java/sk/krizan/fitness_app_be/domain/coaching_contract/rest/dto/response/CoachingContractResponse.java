package sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractStatus;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;

@Builder
@FieldNameConstants
public record CoachingContractResponse(
        Long id,
        ProfileSimpleResponse coach,
        ProfileSimpleResponse client,
        CoachingContractStatus status
) {
}
