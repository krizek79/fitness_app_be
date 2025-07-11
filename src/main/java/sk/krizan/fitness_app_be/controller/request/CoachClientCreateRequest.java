package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CoachClientCreateRequest(
        @NotNull
        Long coachId,
        @NotNull
        Long clientId
) {
}
