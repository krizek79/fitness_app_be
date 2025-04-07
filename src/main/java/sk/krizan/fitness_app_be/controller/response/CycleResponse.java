package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record CycleResponse(
        Long id,
        String name,
        Long authorId,
        String authorName,
        Long traineeId,
        String traineeName,
        Integer numberOfWeeks,
        String description,
        String levelValue
) {
}
