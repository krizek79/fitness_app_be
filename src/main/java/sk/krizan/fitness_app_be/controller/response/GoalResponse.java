package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

@Builder
public record GoalResponse(
        Long id,
        Long cycleId,
        Boolean achieved,
        String text
) {
}
