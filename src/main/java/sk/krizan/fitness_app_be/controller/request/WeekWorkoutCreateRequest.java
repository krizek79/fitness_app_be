package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WeekWorkoutCreateRequest(
        @NotNull
        Long weekId,
        @NotNull
        Long workoutId,
        @Min(1)
        @Max(7)
        @NotNull
        Integer dayOfTheWeek
) {
}
