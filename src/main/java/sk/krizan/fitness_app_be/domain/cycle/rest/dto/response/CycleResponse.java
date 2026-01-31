package sk.krizan.fitness_app_be.domain.cycle.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

@Builder
@FieldNameConstants
public record CycleResponse(
        Long id,
        String name,
        Long authorId,
        String authorName,
        Long traineeId,
        String traineeName,
        Integer numberOfWeeks,
        String description,
        ReferenceDataResponse levelResponse
) {
}
