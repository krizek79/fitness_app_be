package sk.krizan.fitness_app_be.domain.week.rest.dto.response;

import lombok.Builder;

@Builder
public record WeekSimpleResponse(
        Long id,
        Long planId,
        Integer order,
        Boolean completed,
        Integer numberOfWorkouts,
        Integer numberOfCompletedWorkouts
) {
}
