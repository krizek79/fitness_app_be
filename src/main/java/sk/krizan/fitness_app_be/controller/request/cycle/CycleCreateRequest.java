package sk.krizan.fitness_app_be.controller.request.cycle;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CycleCreateRequest(
        Long traineeId,
        @NotEmpty
        @Size(max = 255)
        String title
) {
}
