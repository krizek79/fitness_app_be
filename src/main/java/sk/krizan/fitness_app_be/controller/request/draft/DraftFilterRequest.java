package sk.krizan.fitness_app_be.controller.request.draft;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.DraftEntityType;

@Builder
public record DraftFilterRequest(
        @NotNull
        @Min(0)
        Integer page,
        @NotNull
        @Min(1)
        Integer size,
        @NotNull
        String sortBy,
        @NotNull
        String sortDirection,
        String title,
        @NotNull
        DraftEntityType entityType
) {
}
