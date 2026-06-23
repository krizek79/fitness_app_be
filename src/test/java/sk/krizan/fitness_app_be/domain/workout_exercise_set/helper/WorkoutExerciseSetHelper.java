package sk.krizan.fitness_app_be.domain.workout_exercise_set.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.common.helper.RandomHelper;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.response.WorkoutExerciseSetResponse;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkoutExerciseSetHelper {

    public static WorkoutExerciseSet createWorkoutExerciseSet(Integer order) {
        WorkoutExerciseSet workoutExerciseSet = new WorkoutExerciseSet();
        workoutExerciseSet.setOrder(order);
        workoutExerciseSet.setWorkoutExerciseSetType(WorkoutExerciseSetType.STRAIGHT_SET);
        workoutExerciseSet.setGoalRepetitions(RandomHelper.getRandomInt(1, 15));
        workoutExerciseSet.setGoalWeight(RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(0.125), BigDecimal.valueOf(150), 3));
        workoutExerciseSet.setGoalTimeSeconds(Duration.ofMinutes(45));
        workoutExerciseSet.setRestDurationSeconds(Duration.ofMinutes(5));
        workoutExerciseSet.setNote(UUID.randomUUID().toString());

        return workoutExerciseSet;
    }

    public static WorkoutExerciseSetInputRequest createInputRequest(
            Long id,
            WorkoutExerciseSetType workoutExerciseSetType,
            Integer order,
            BigDecimal goalWeight,
            Integer goalRepetitions,
            Long goalTimeSeconds,
            Long restDurationSeconds
    ) {
        return WorkoutExerciseSetInputRequest.builder()
                .id(id)
                .workoutExerciseSetType(workoutExerciseSetType)
                .order(order)
                .goalWeight(goalWeight)
                .goalRepetitions(goalRepetitions)
                .goalTimeSeconds(goalTimeSeconds)
                .restDurationSeconds(restDurationSeconds)
                .note(UUID.randomUUID().toString())
                .build();
    }

    public static void assertWorkoutExerciseSetResponse(WorkoutExerciseSet workoutExerciseSet, WorkoutExerciseSetResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutExerciseSet.getId(), response.id());
        Assertions.assertEquals(workoutExerciseSet.getWorkoutExercise().getId(), response.workoutExerciseId());
        ReferenceDataHelper.assertReferenceDataResponse(workoutExerciseSet.getWorkoutExerciseSetType(), response.workoutExerciseSetType());
        Assertions.assertEquals(workoutExerciseSet.getOrder(), response.order());
        Assertions.assertEquals(workoutExerciseSet.getGoalWeight(), response.goalWeight());
        Assertions.assertEquals(workoutExerciseSet.getGoalRepetitions(), response.goalRepetitions());

        if (workoutExerciseSet.getGoalTimeSeconds() != null) {
            Assertions.assertEquals(workoutExerciseSet.getGoalTimeSeconds().toSeconds(), response.goalTimeSeconds());
        }

        if (workoutExerciseSet.getRestDurationSeconds() != null) {
            Assertions.assertEquals(workoutExerciseSet.getRestDurationSeconds().toSeconds(), response.restDurationSeconds());
        }

        Assertions.assertEquals(workoutExerciseSet.getNote(), response.note());
    }

    public static void assertInputToEntity(
            WorkoutExerciseSet workoutExerciseSet,
            WorkoutExerciseSetInputRequest request
    ) {
        Assertions.assertNotNull(workoutExerciseSet);

        if (request.id() != null) {
            Assertions.assertEquals(request.id(), workoutExerciseSet.getId());
        } else {
            Assertions.assertNotNull(workoutExerciseSet.getId());
        }

        Assertions.assertEquals(request.workoutExerciseSetType(), workoutExerciseSet.getWorkoutExerciseSetType());
        Assertions.assertEquals(request.order(), workoutExerciseSet.getOrder());
        Assertions.assertEquals(request.goalWeight(), workoutExerciseSet.getGoalWeight());
        Assertions.assertEquals(request.goalRepetitions(), workoutExerciseSet.getGoalRepetitions());

        if (request.goalTimeSeconds() != null) {
            Assertions.assertEquals(request.goalTimeSeconds(), workoutExerciseSet.getGoalTimeSeconds().toSeconds());
        }

        if (request.restDurationSeconds() != null) {
            Assertions.assertEquals(request.restDurationSeconds(), workoutExerciseSet.getRestDurationSeconds().toSeconds());
        }

        Assertions.assertEquals(request.note(), workoutExerciseSet.getNote());
    }

    public static void assertClone(WorkoutExerciseSet original, WorkoutExerciseSet clone) {
        Assertions.assertNotNull(clone);
        Assertions.assertNotEquals(original.getWorkoutExercise(), clone.getWorkoutExercise());
        Assertions.assertNotEquals(original.getId(), clone.getId());
        Assertions.assertEquals(original.getOrder(), clone.getOrder());
        Assertions.assertEquals(original.getWorkoutExerciseSetType(), clone.getWorkoutExerciseSetType());
        Assertions.assertEquals(original.getRestDurationSeconds(), clone.getRestDurationSeconds());
        Assertions.assertNull(clone.getNote());
    }

}
