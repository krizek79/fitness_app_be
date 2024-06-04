package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record WorkoutUpdateRequest(
        @NotNull
        Long id,
        @NotEmpty
        String name,
        @NotEmpty
        String levelKey,
        @Size(max = 1000)
        String description,
        Set<String> tagNames
) {
}
