package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record GoalUpdateRequest(
        @NotEmpty
        @Size(max = 1000)
        String text
) {
}
