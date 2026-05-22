package sk.krizan.fitness_app_be.domain.week.rest.dto.response;

import lombok.Builder;

@Builder
public record WeekResponse(
        Long id,
        Long planId,
        Integer order,
        Boolean completed,
        String note,
        Integer numberOfWorkouts,
        Integer numberOfCompletedWorkouts
) {
}
