package sk.krizan.fitness_app_be.domain.workout.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutResponse(
        Long id,
        String name,
        Long authorId,
        String authorName,
        Long traineeId,
        String traineeName,
        List<TagResponse> tagResponseList,
        String description,
        Boolean isTemplate,
        ReferenceDataResponse weightUnitResponse,
        String note
) {
}
