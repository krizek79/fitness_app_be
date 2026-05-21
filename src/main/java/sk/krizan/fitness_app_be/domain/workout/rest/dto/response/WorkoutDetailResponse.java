package sk.krizan.fitness_app_be.domain.workout.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutDetailResponse(
        Long id,
        String title,
        ProfileSimpleResponse author,
        ProfileSimpleResponse trainee,
        List<TagResponse> tags,
        String description,
        Boolean isTemplate,
        ReferenceDataResponse weightUnit,
        String note,
        List<WorkoutExerciseResponse> workoutExercises
) {
}
