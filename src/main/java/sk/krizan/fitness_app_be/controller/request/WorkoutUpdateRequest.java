package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record WorkoutUpdateRequest(
        @NotEmpty
        String name,
        @Size(max = 1000)
        String description,
        String weightUnitKey,
        Set<String> tagNames
) {
}
