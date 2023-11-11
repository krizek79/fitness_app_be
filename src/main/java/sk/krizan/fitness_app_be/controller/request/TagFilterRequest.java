package sk.krizan.fitness_app_be.controller.request;

import lombok.Builder;

@Builder
public record TagFilterRequest(
    Integer page,
    Integer size,
    String sortBy,
    String sortDirection,
    String name
) {
}
