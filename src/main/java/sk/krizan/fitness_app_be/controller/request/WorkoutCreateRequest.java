package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record WorkoutCreateRequest(
        @NotEmpty
        @Size(min = 1, max = 64)
        String name,
        @NotNull
        Boolean isTemplate,
        @NotEmpty
        String weightUnitKey
) {
}
