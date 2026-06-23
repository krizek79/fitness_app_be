package sk.krizan.fitness_app_be.domain.workout_exercise_session.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.workout_exercise.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.response.WorkoutExerciseSessionResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.entity.WorkoutExerciseSetResult;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.mapper.WorkoutExerciseSetResultMapper;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;

import java.util.Comparator;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseSessionMapper {

    public static WorkoutExerciseSessionResponse entityToResponse(WorkoutExerciseSession workoutExerciseSession) {
        return WorkoutExerciseSessionResponse.builder()
                .id(workoutExerciseSession.getId())
                .workoutSessionId(workoutExerciseSession.getWorkoutSession().getId())
                .workoutExercise(WorkoutExerciseMapper.entityToResponse(workoutExerciseSession.getWorkoutExercise()))
                .order(workoutExerciseSession.getOrder())
                .note(workoutExerciseSession.getNote())
                .workoutExerciseSetResults(
                        workoutExerciseSession.getWorkoutExerciseSetResults().stream()
                                .sorted(Comparator.comparingInt(WorkoutExerciseSetResult::getOrder))
                                .map(WorkoutExerciseSetResultMapper::entityToResponse)
                                .toList())
                .build();
    }

    public static WorkoutExerciseSession inputRequestToEntity(
            WorkoutExerciseSession workoutExerciseSession,
            WorkoutExerciseSessionInputRequest request,
            WorkoutSession workoutSession
    ) {
        if (workoutExerciseSession == null) {
            workoutExerciseSession = new WorkoutExerciseSession();
            workoutSession.addToWorkoutExerciseSessions(workoutExerciseSession);
        }

        workoutExerciseSession.setOrder(request.order());
        workoutExerciseSession.setNote(request.note());

        return workoutExerciseSession;
    }

}
