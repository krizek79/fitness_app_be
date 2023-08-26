package sk.krizan.fitness_app_be.controller.response;

import java.time.Instant;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
    String token,
    Instant expiresAt,
    UserResponse userResponse
) {
}
