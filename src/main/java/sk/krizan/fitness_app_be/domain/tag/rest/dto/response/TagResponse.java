package sk.krizan.fitness_app_be.domain.tag.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record TagResponse(
        Long id,
        String name
) {
}
