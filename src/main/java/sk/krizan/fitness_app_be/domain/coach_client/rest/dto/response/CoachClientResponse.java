package sk.krizan.fitness_app_be.domain.coach_client.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@Builder
@FieldNameConstants
public record CoachClientResponse(
        Long id,
        Long coachId,
        String coachName,
        String coachPictureUrl,
        Long clientId,
        String clientName,
        String clientPictureUrl,
        Instant startedAt,
        Boolean active
) {
}
