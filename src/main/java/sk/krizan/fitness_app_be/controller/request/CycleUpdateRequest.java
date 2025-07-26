package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.Level;

@Builder
public record CycleUpdateRequest(
        @NotEmpty
        @Size(max = 255)
        String name,
        @Size(max = 2000)
        String description,
        @NotEmpty
        Level level,
        Long traineeId
) {
}
