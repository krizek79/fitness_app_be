package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

import java.time.Instant;

@Builder
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
