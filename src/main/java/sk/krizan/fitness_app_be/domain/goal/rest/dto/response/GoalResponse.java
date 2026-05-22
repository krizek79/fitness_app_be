package sk.krizan.fitness_app_be.domain.goal.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record GoalResponse(
        Long id,
        Long planId,
        Boolean achieved,
        String text
) {
}
