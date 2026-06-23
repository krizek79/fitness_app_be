package sk.krizan.fitness_app_be.domain.workout_exercise_set.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.response.WorkoutExerciseSetResponse;

import java.time.Duration;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseSetMapper {

    public static WorkoutExerciseSetResponse entityToResponse(WorkoutExerciseSet workoutExerciseSet) {
        return WorkoutExerciseSetResponse.builder()
                .id(workoutExerciseSet.getId())
                .workoutExerciseId(workoutExerciseSet.getWorkoutExercise().getId())
                .order(workoutExerciseSet.getOrder())
                .workoutExerciseSetType(ReferenceDataMapper.enumToResponse(workoutExerciseSet.getWorkoutExerciseSetType()))
                .goalRepetitions(workoutExerciseSet.getGoalRepetitions())
                .goalWeight(workoutExerciseSet.getGoalWeight())
                .goalTimeSeconds(workoutExerciseSet.getGoalTimeSeconds() != null ? workoutExerciseSet.getGoalTimeSeconds().toSeconds() : null)
                .goalDistanceMeters(workoutExerciseSet.getGoalDistanceMeters())
                .restDurationSeconds(workoutExerciseSet.getRestDurationSeconds() != null ? workoutExerciseSet.getRestDurationSeconds().toSeconds() : null)
                .note(workoutExerciseSet.getNote())
                .build();
    }

    public static void inputRequestToEntity(WorkoutExerciseSet workoutExerciseSet, WorkoutExerciseSetInputRequest request, WorkoutExercise workoutExercise) {
        if (workoutExerciseSet == null) {
            workoutExerciseSet = new WorkoutExerciseSet();
            workoutExercise.addToWorkoutExerciseSets(workoutExerciseSet);
        }

        workoutExerciseSet.setWorkoutExerciseSetType(request.workoutExerciseSetType());
        workoutExerciseSet.setGoalRepetitions(request.goalRepetitions());
        workoutExerciseSet.setGoalWeight(request.goalWeight());
        workoutExerciseSet.setGoalTimeSeconds(request.goalTimeSeconds() != null ? Duration.ofSeconds(request.goalTimeSeconds()) : null);
        workoutExerciseSet.setGoalDistanceMeters(request.goalDistanceMeters());
        workoutExerciseSet.setRestDurationSeconds(request.restDurationSeconds() != null ? Duration.ofSeconds(request.restDurationSeconds()) : null);
        workoutExerciseSet.setNote(request.note());
        workoutExerciseSet.setOrder(request.order());
    }

}
