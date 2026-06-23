package sk.krizan.fitness_app_be.domain.workout_exercise_set_result.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.entity.WorkoutExerciseSetResult;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request.WorkoutExerciseSetResultInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.response.WorkoutExerciseSetResultResponse;

import java.time.Duration;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseSetResultMapper {

    public static WorkoutExerciseSetResultResponse entityToResponse(WorkoutExerciseSetResult result) {
        return WorkoutExerciseSetResultResponse.builder()
                .id(result.getId())
                .workoutExerciseSessionId(result.getWorkoutExerciseSession().getId())
                .workoutExerciseSetId(result.getWorkoutExerciseSet() != null ? result.getWorkoutExerciseSet().getId() : null)
                .order(result.getOrder())
                .workoutExerciseSetType(ReferenceDataMapper.enumToResponse(result.getWorkoutExerciseSetType()))
                .repetitions(result.getRepetitions())
                .weight(result.getWeight())
                .timeSeconds(result.getTimeSeconds() != null ? result.getTimeSeconds().toSeconds() : null)
                .distanceMeters(result.getDistanceMeters())
                .restDurationSeconds(result.getRestDurationSeconds() != null ? result.getRestDurationSeconds().toSeconds() : null)
                .completed(result.getCompleted())
                .note(result.getNote())
                .build();
    }

    public static void inputRequestToEntity(
            WorkoutExerciseSetResult result,
            WorkoutExerciseSetResultInputRequest request,
            WorkoutExerciseSession workoutExerciseSession,
            WorkoutExerciseSet workoutExerciseSet
    ) {
        if (result == null) {
            result = new WorkoutExerciseSetResult();
            workoutExerciseSession.addToWorkoutExerciseSetResults(result);
        }

        result.setWorkoutExerciseSet(workoutExerciseSet);
        result.setOrder(request.order());
        result.setWorkoutExerciseSetType(request.workoutExerciseSetType());
        result.setRepetitions(request.repetitions());
        result.setWeight(request.weight());
        result.setTimeSeconds(request.timeSeconds() != null ? Duration.ofSeconds(request.timeSeconds()) : null);
        result.setDistanceMeters(request.distanceMeters());
        result.setRestDurationSeconds(request.restDurationSeconds() != null ? Duration.ofSeconds(request.restDurationSeconds()) : null);
        result.setCompleted(request.completed() != null ? request.completed() : false);
        result.setNote(request.note());
    }

}
