package sk.krizan.fitness_app_be.domain.profile.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record ProfileSimpleResponse(
        Long id,
        String name
) {
}
