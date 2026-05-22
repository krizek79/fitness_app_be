package sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;

import java.time.Instant;

@Builder
@FieldNameConstants
public record CoachingContractResponse(
        Long id,
        ProfileSimpleResponse coach,
        ProfileSimpleResponse client,
        Instant startedAt,
        Boolean active
) {
}
