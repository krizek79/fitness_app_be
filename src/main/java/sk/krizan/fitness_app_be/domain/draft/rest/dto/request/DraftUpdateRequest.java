package sk.krizan.fitness_app_be.domain.draft.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@Builder
@FieldNameConstants
public record DraftUpdateRequest(
        @NotNull
        Map<String, Object> content
) {
}
