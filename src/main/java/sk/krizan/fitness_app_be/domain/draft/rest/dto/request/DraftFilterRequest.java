package sk.krizan.fitness_app_be.domain.draft.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.draft.entity.DraftEntityType;

@Builder
@FieldNameConstants
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
