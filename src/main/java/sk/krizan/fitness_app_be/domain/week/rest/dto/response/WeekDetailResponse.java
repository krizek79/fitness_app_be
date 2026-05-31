package sk.krizan.fitness_app_be.domain.week.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record WeekDetailResponse(
        Long id,
        Long planId,
        Integer order,
        String note,
        Boolean completed,
        List<WeekWorkoutResponse> weekWorkouts
) {
}
