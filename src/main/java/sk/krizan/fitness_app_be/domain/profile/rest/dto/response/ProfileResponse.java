package sk.krizan.fitness_app_be.domain.profile.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

@Builder
@FieldNameConstants
public record ProfileResponse(
        Long id,
        Long userId,
        String name,
        String profilePictureUrl,
        ReferenceDataResponse preferredWeightUnitResponse
) {
}
