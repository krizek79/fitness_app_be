package sk.krizan.fitness_app_be.domain.workout_exercise_session.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.response.WorkoutExerciseSessionResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request.WorkoutExerciseSetResultInputRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkoutExerciseSessionHelper {

    public static WorkoutExerciseSessionInputRequest createInputRequest(
            Long id,
            Long workoutExerciseId,
            Integer order,
            String note,
            List<WorkoutExerciseSetResultInputRequest> workoutExerciseSetResults
    ) {
        return WorkoutExerciseSessionInputRequest.builder()
                .id(id)
                .workoutExerciseId(workoutExerciseId)
                .order(order)
                .note(note)
                .workoutExerciseSetResults(workoutExerciseSetResults)
                .build();
    }

    public static void assertWorkoutExerciseSessionResponse(WorkoutExerciseSession workoutExerciseSession, WorkoutExerciseSessionResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutExerciseSession.getId(), response.id());
        Assertions.assertEquals(workoutExerciseSession.getWorkoutSession().getId(), response.workoutSessionId());
        Assertions.assertEquals(workoutExerciseSession.getOrder(), response.order());
        Assertions.assertEquals(workoutExerciseSession.getNote(), response.note());
        Assertions.assertEquals(workoutExerciseSession.getWorkoutExercise().getId(), response.workoutExercise().id());
    }

}
