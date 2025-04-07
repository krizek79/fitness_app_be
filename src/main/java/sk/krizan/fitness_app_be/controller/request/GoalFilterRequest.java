package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record GoalFilterRequest(
        @NotNull
        Integer page,
        @NotNull
        Integer size,
        @NotNull
        String sortBy,
        @NotNull
        @Pattern(regexp = "ASC|DESC|asc|desc")
        String sortDirection,
        @NotNull
        Long cycleId
) {
}
