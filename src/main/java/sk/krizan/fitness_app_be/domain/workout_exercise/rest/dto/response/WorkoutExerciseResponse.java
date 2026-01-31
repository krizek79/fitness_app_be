package sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.response.WorkoutExerciseSetResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutExerciseResponse(
        Long id,
        Long workoutId,
        Integer order,
        String exerciseName,
        ReferenceDataResponse workoutExerciseTypeResponse,
        String note,
        List<WorkoutExerciseSetResponse> workoutExerciseSetResponseList
) {
}
