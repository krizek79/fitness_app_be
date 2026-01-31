package sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record CoachClientCreateRequest(
        @NotNull
        Long coachId,
        @NotNull
        Long clientId
) {
}
