package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record GoalCreateRequest(
        @NotEmpty
        @Size(max = 1000)
        String text,
        @NotNull
        Long cycleId
) {
}
