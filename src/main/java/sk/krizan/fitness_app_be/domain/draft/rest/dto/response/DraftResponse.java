package sk.krizan.fitness_app_be.domain.draft.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.draft.entity.DraftEntityType;

import java.util.Map;

@Builder
@FieldNameConstants
public record DraftResponse(
        Long id,
        Long profileId,
        DraftEntityType entityType,
        String title,
        Map<String, Object> content
) {
}
