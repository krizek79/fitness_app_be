package sk.krizan.fitness_app_be.controller.request.draft;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.DraftEntityType;

import java.util.Map;

@Builder
public record DraftCreateRequest(
        @NotNull
        DraftEntityType entityType,
        @NotNull
        Map<String, Object> content
) {
}
