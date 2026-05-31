package sk.krizan.fitness_app_be.domain.plan.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekSimpleResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record PlanDetailResponse(
        Long id,
        String title,
        String description,
        ProfileSimpleResponse author,
        ProfileSimpleResponse trainee,
        List<WeekSimpleResponse> weeks
) {
}
