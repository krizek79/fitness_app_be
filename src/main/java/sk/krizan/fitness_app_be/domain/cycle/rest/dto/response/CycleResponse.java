package sk.krizan.fitness_app_be.domain.cycle.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

@Builder
@FieldNameConstants
public record CycleResponse(
        Long id,
        String title,
        ProfileSimpleResponse author,
        ProfileSimpleResponse trainee,
        Integer numberOfWeeks,
        String description,
        ReferenceDataResponse level
) {
}
