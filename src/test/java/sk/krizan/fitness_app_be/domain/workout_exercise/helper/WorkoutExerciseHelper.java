package sk.krizan.fitness_app_be.domain.workout_exercise.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExerciseType;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.helper.WorkoutExerciseSetHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.response.WorkoutExerciseSetResponse;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkoutExerciseHelper {

    public static WorkoutExercise createWorkoutExercise(
            Exercise exercise,
            List<WorkoutExerciseSet> workoutExerciseSets,
            Integer order
    ) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);
        workoutExercise.setOrder(order);
        workoutExercise.setNote(UUID.randomUUID().toString());
        workoutExercise.setWorkoutExerciseType(WorkoutExerciseType.WEIGHT);

        workoutExerciseSets.forEach(workoutExercise::addToWorkoutExerciseSets);

        return workoutExercise;
    }

    public static WorkoutExerciseInputRequest createInputRequest(
            Long id,
            Long exerciseId,
            Integer order,
            List<WorkoutExerciseSetInputRequest> workoutExerciseSets
    ) {
        return WorkoutExerciseInputRequest.builder()
                .id(id)
                .exerciseId(exerciseId)
                .order(order)
                .note(UUID.randomUUID().toString())
                .workoutExerciseType(WorkoutExerciseType.WEIGHT)
                .workoutExerciseSets(workoutExerciseSets)
                .build();
    }

    public static void assertWorkoutExerciseResponse(WorkoutExercise workoutExercise, WorkoutExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutExercise.getId(), response.id());
        Assertions.assertEquals(workoutExercise.getWorkout().getId(), response.workoutId());
        Assertions.assertEquals(workoutExercise.getExercise().getName(), response.exerciseName());
        Assertions.assertEquals(workoutExercise.getNote(), response.note());
        ReferenceDataHelper.assertReferenceDataResponse(workoutExercise.getWorkoutExerciseType(), response.workoutExerciseType());
        Assertions.assertNotNull(response.workoutExerciseSets());

        Assertions.assertEquals(workoutExercise.getWorkoutExerciseSets().size(), response.workoutExerciseSets().size());
        List<WorkoutExerciseSet> sortedWorkoutExerciseSetList = workoutExercise.getWorkoutExerciseSets().stream()
                .sorted(Comparator.comparing(WorkoutExerciseSet::getOrder))
                .toList();

        for (int i = 0; i < response.workoutExerciseSets().size(); i++) {
            WorkoutExerciseSet expectedWorkoutExerciseSet = sortedWorkoutExerciseSetList.get(i);
            WorkoutExerciseSetResponse workoutExerciseSetResponse = response.workoutExerciseSets().get(i);
            WorkoutExerciseSetHelper.assertWorkoutExerciseSetResponse(expectedWorkoutExerciseSet, workoutExerciseSetResponse);
        }
    }

    public static void assertInputToEntity(WorkoutExercise workoutExercise, WorkoutExerciseInputRequest request) {
        Assertions.assertNotNull(workoutExercise);

        if (request.id() != null) {
            Assertions.assertEquals(request.id(), workoutExercise.getId());
        } else {
            Assertions.assertNotNull(workoutExercise.getId());
        }

        Assertions.assertEquals(request.note(), workoutExercise.getNote());
        Assertions.assertEquals(request.workoutExerciseType(), workoutExercise.getWorkoutExerciseType());

        //  questionable assert as order might be different due to user input and reordering in service layer
        Assertions.assertEquals(request.order(), workoutExercise.getOrder());

        Assertions.assertEquals(request.exerciseId(), workoutExercise.getExercise().getId());

        assertWorkoutExerciseSets(workoutExercise, request);
    }

    private static void assertWorkoutExerciseSets(WorkoutExercise workoutExercise, WorkoutExerciseInputRequest request) {
        Assertions.assertEquals(request.workoutExerciseSets().size(), workoutExercise.getWorkoutExerciseSets().size());
        List<WorkoutExerciseSet> sortedWorkoutExerciseSets = workoutExercise.getWorkoutExerciseSets().stream()
                .sorted(Comparator.comparing(WorkoutExerciseSet::getOrder))
                .toList();
        List<WorkoutExerciseSetInputRequest> sortedWorkoutExerciseSetInputRequests = request.workoutExerciseSets().stream()
                .sorted(Comparator.comparing(WorkoutExerciseSetInputRequest::order))
                .toList();

        for (int i = 0; i < sortedWorkoutExerciseSets.size(); i++) {
            WorkoutExerciseSet workoutExerciseSet = sortedWorkoutExerciseSets.get(i);
            WorkoutExerciseSetInputRequest workoutExerciseSetInputRequest = sortedWorkoutExerciseSetInputRequests.get(i);
            WorkoutExerciseSetHelper.assertInputToEntity(workoutExerciseSet, workoutExerciseSetInputRequest);
        }
    }

    public static void assertClone(WorkoutExercise original, WorkoutExercise clone) {
        Assertions.assertNotNull(clone);
        Assertions.assertNotEquals(original.getWorkout(), clone.getWorkout());
        Assertions.assertNotNull(clone.getId());
        Assertions.assertNotEquals(original.getId(), clone.getId());
        Assertions.assertEquals(original.getExercise().getId(), clone.getExercise().getId());
        Assertions.assertNull(clone.getNote());
        Assertions.assertEquals(original.getOrder(), clone.getOrder());
        Assertions.assertEquals(original.getWorkoutExerciseType(), clone.getWorkoutExerciseType());

        assertCloneWorkoutExerciseSets(original.getWorkoutExerciseSets(), clone.getWorkoutExerciseSets());
    }

    private static void assertCloneWorkoutExerciseSets(List<WorkoutExerciseSet> originalSets, List<WorkoutExerciseSet> cloneSets) {
        Assertions.assertEquals(originalSets.size(), cloneSets.size());
        List<WorkoutExerciseSet> sortedOriginalWorkoutExerciseSets = originalSets.stream()
                .sorted(Comparator.comparing(WorkoutExerciseSet::getOrder))
                .toList();
        List<WorkoutExerciseSet> sortedCloneWorkoutExerciseSets = cloneSets.stream()
                .sorted(Comparator.comparing(WorkoutExerciseSet::getOrder))
                .toList();

        for (int i = 0; i < sortedOriginalWorkoutExerciseSets.size(); i++) {
            WorkoutExerciseSetHelper.assertClone(sortedOriginalWorkoutExerciseSets.get(i), sortedCloneWorkoutExerciseSets.get(i));
        }
    }

}
