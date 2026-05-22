package sk.krizan.fitness_app_be.domain.plan.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;

@Builder
@FieldNameConstants
public record PlanResponse(
        Long id,
        String title,
        ProfileSimpleResponse author,
        ProfileSimpleResponse trainee,
        Integer numberOfWeeks,
        String description
) {
}
