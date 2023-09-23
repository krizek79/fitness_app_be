package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record TagResponse(
    Long id,
    String name
) {
}
