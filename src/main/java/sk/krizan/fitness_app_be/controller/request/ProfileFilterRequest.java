package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProfileFilterRequest(
        @NotNull
        Integer page,
        @NotNull
        Integer size,
        @NotNull
        String sortBy,
        @NotNull
        String sortDirection,
        String name
) {
}
