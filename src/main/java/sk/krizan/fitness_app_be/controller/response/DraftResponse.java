package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.DraftEntityType;

import java.util.Map;

@Builder
public record DraftResponse(
        Long id,
        Long profileId,
        DraftEntityType entityType,
        String title,
        Map<String, Object> content
) {
}
