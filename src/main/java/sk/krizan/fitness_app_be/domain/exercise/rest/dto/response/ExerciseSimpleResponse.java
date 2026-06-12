package sk.krizan.fitness_app_be.domain.exercise.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record ExerciseSimpleResponse(

        Long id,

        String title,

        String thumbnailUrl,

        ReferenceDataResponse exerciseCategory,

        List<ReferenceDataResponse> primaryMuscles

) {
}
