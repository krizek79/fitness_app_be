package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.Level;

@Builder
public record CycleFilterRequest(
        @NotNull
        @Min(0)
        Integer page,
        @NotNull
        @Min(1)
        Integer size,
        @NotNull
        String sortBy,
        @NotNull
        @Pattern(regexp = "ASC|DESC|asc|desc")
        String sortDirection,
        Long authorId,
        Long traineeId,
        String name,
        Level level
) {
}
