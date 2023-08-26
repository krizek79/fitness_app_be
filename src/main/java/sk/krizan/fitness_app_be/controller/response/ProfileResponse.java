package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record ProfileResponse(
    Long id,
    Long userId,
    String displayName,
    String profilePictureUrl
) {
}
