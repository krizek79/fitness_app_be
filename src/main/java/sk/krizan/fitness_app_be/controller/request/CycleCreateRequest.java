package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CycleCreateRequest(
        @NotEmpty
        @Size(max = 255)
        String name
) {
}
