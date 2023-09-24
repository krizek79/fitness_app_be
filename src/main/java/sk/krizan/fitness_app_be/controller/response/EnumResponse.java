package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record EnumResponse(
    String key,
    String value
) {
}
