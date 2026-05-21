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

    public static WorkoutExerciseSet createWorkoutExerciseSet(
            Integer order,
            Boolean isWorkoutTemplate
    ) {
        WorkoutExerciseSet workoutExerciseSet = new WorkoutExerciseSet();
        workoutExerciseSet.setOrder(order);
        workoutExerciseSet.setWorkoutExerciseSetType(WorkoutExerciseSetType.STRAIGHT_SET);

        workoutExerciseSet.setGoalRepetitions(RandomHelper.getRandomInt(1, 15));
        workoutExerciseSet.setGoalWeight(RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(0.125), BigDecimal.valueOf(150), 3));
        workoutExerciseSet.setGoalTime(Duration.ofMinutes(45));

        if (!isWorkoutTemplate) {
            workoutExerciseSet.setActualRepetitions(RandomHelper.getRandomInt(1, 15));
            workoutExerciseSet.setActualWeight(RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(0.125), BigDecimal.valueOf(150), 3));
            workoutExerciseSet.setActualTime(Duration.ofMinutes(41));
        }

        workoutExerciseSet.setRestDuration(Duration.ofMinutes(5));
        workoutExerciseSet.setNote(UUID.randomUUID().toString());

        return workoutExerciseSet;
    }

    public static WorkoutExerciseSetInputRequest createInputRequest(
            Long id,
            WorkoutExerciseSetType workoutExerciseSetType,
            Boolean completed,
            Integer order,
            BigDecimal goalWeight,
            BigDecimal actualWeight,
            Integer goalRepetitions,
            Integer actualRepetitions,
            Long goalTimeSeconds,
            Long actualTimeSeconds,
            Long restDurationSeconds
    ) {
        return WorkoutExerciseSetInputRequest.builder()
                .id(id)
                .workoutExerciseSetType(workoutExerciseSetType)
                .completed(completed)
                .order(order)
                .goalWeight(goalWeight)
                .actualWeight(actualWeight)
                .goalRepetitions(goalRepetitions)
                .actualRepetitions(actualRepetitions)
                .goalTimeSeconds(goalTimeSeconds)
                .actualTimeSeconds(actualTimeSeconds)
                .restDurationSeconds(restDurationSeconds)
                .note(UUID.randomUUID().toString())
                .build();
    }

    public static void assertWorkoutExerciseSetResponse(WorkoutExerciseSet workoutExerciseSet, WorkoutExerciseSetResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutExerciseSet.getId(), response.id());
        Assertions.assertEquals(workoutExerciseSet.getWorkoutExercise().getId(), response.workoutExerciseId());
        ReferenceDataHelper.assertReferenceDataResponse(workoutExerciseSet.getWorkoutExerciseSetType(), response.workoutExerciseSetType());
        Assertions.assertEquals(workoutExerciseSet.getCompleted(), response.completed());
        Assertions.assertEquals(workoutExerciseSet.getOrder(), response.order());
        Assertions.assertEquals(workoutExerciseSet.getGoalWeight(), response.goalWeight());
        Assertions.assertEquals(workoutExerciseSet.getActualWeight(), response.actualWeight());
        Assertions.assertEquals(workoutExerciseSet.getGoalRepetitions(), response.goalRepetitions());
        Assertions.assertEquals(workoutExerciseSet.getActualRepetitions(), response.actualRepetitions());

        if (workoutExerciseSet.getGoalTime() != null) {
            Assertions.assertEquals(workoutExerciseSet.getGoalTime().toSeconds(), response.goalTimeSeconds());
        }

        if (workoutExerciseSet.getActualTime() != null) {
            Assertions.assertEquals(workoutExerciseSet.getActualTime().toSeconds(), response.actualTimeSeconds());
        }

        if (workoutExerciseSet.getRestDuration() != null) {
            Assertions.assertEquals(workoutExerciseSet.getRestDuration().toSeconds(), response.restDurationSeconds());
        }

        Assertions.assertEquals(workoutExerciseSet.getNote(), response.note());
    }

    //  assert input to entity
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
        Assertions.assertEquals(request.completed(), workoutExerciseSet.getCompleted());

        //  questionable assert as order might be different due to user input and reordering in service layer
        Assertions.assertEquals(request.order(), workoutExerciseSet.getOrder());

        Assertions.assertEquals(request.goalWeight(), workoutExerciseSet.getGoalWeight());
        Assertions.assertEquals(request.actualWeight(), workoutExerciseSet.getActualWeight());
        Assertions.assertEquals(request.goalRepetitions(), workoutExerciseSet.getGoalRepetitions());
        Assertions.assertEquals(request.actualRepetitions(), workoutExerciseSet.getActualRepetitions());

        if (request.goalTimeSeconds() != null) {
            Assertions.assertEquals(request.goalTimeSeconds(), workoutExerciseSet.getGoalTime().toSeconds());
        }

        if (request.actualTimeSeconds() != null) {
            Assertions.assertEquals(request.actualTimeSeconds(), workoutExerciseSet.getActualTime().toSeconds());
        }

        if (request.restDurationSeconds() != null) {
            Assertions.assertEquals(request.restDurationSeconds(), workoutExerciseSet.getRestDuration().toSeconds());
        }

        Assertions.assertEquals(request.note(), workoutExerciseSet.getNote());
    }

    public static void assertClone(WorkoutExerciseSet original, WorkoutExerciseSet clone) {
        Assertions.assertNotNull(clone);
        Assertions.assertNotEquals(original.getWorkoutExercise(), clone.getWorkoutExercise());
        Assertions.assertNotEquals(original.getId(), clone.getId());
        Assertions.assertEquals(original.getOrder(), clone.getOrder());
        Assertions.assertEquals(original.getWorkoutExerciseSetType(), clone.getWorkoutExerciseSetType());
        Assertions.assertFalse(clone.getCompleted());
        Assertions.assertEquals(original.getGoalWeight(), clone.getGoalWeight());
        Assertions.assertEquals(original.getGoalRepetitions(), clone.getGoalRepetitions());
        Assertions.assertEquals(original.getGoalTime(), clone.getGoalTime());
        Assertions.assertNull(clone.getActualWeight());
        Assertions.assertNull(clone.getActualRepetitions());
        Assertions.assertNull(clone.getActualTime());
        Assertions.assertEquals(original.getRestDuration(), clone.getRestDuration());
        Assertions.assertNull(clone.getNote());
    }

}
