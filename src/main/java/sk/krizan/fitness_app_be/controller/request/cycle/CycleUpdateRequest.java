package sk.krizan.fitness_app_be.controller.request.cycle;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.Level;

@Builder
public record CycleUpdateRequest(
        @NotEmpty
        @Size(max = 255)
        String title,
        @Size(max = 2000)
        String description,
        @NotNull
        Level level,
        Long traineeId
) {
}
