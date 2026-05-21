package sk.krizan.fitness_app_be.domain.workout.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutSimpleResponse(
        Long id,
        String title,
        ProfileSimpleResponse author,
        ProfileSimpleResponse trainee,
        List<TagResponse> tags,
        Boolean isTemplate
) {
}
