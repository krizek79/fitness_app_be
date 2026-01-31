package sk.krizan.fitness_app_be.domain.reference.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record ReferenceDataResponse(
        String key,
        String value
) {
}
