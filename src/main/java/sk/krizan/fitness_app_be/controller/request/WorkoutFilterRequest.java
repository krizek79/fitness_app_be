package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

@Builder
public record WorkoutFilterRequest(
        @NotNull
        Integer page,
        @NotNull
        Integer size,
        @NotNull
        String sortBy,
        @NotNull
        @Pattern(regexp = "ASC|DESC|asc|desc")
        String sortDirection,
        String name,
        List<Long> tagIdList,
        Long authorId,
        Boolean isTemplate
) {
}
