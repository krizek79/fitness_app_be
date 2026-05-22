package sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record CoachingContractFilterRequest(
        @NotNull
        Integer page,
        @NotNull
        Integer size,
        @NotNull
        String sortBy,
        @NotNull
        @Pattern(regexp = "ASC|DESC|asc|desc")
        String sortDirection,
        Long coachId,
        Long clientId
) {
}
