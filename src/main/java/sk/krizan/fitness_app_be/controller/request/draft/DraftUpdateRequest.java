package sk.krizan.fitness_app_be.controller.request.draft;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Map;

@Builder
public record DraftUpdateRequest(
        @NotNull
        Map<String, Object> content
) {
}
