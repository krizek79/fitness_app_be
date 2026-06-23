package sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.response.WorkoutExerciseSetResultResponse;

import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutExerciseSessionResponse(

        Long id,

        Long workoutSessionId,

        WorkoutExerciseResponse workoutExercise,

        Integer order,

        String note,

        List<WorkoutExerciseSetResultResponse> workoutExerciseSetResults

) {}
