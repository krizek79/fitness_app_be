package sk.krizan.fitness_app_be.domain.goal.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;

@Builder
@FieldNameConstants
public record GoalResponse(
        Long id,
        ProfileSimpleResponse profile,
        Boolean achieved,
        String text
) {
}
