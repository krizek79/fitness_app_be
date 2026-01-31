package sk.krizan.fitness_app_be.domain.tag.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record TagCreateRequest(
    @NotEmpty
    String name
) {
}
