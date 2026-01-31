package sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
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
