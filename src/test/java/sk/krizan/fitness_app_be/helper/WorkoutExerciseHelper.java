package sk.krizan.fitness_app_be.helper;

import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WorkoutExerciseHelper {

    public static WorkoutExercise createMockWorkoutExercise(Workout workout, Exercise exercise, int sets, int repetitions, Duration restDuration) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);
        workoutExercise.setSets(sets);
        workoutExercise.setRepetitions(repetitions);
        workoutExercise.setRestDuration(restDuration);

        workout.addToWorkoutExerciseList(List.of(workoutExercise));

        return workoutExercise;
    }

    public static WorkoutExerciseFilterRequest createFilterRequest(Integer page, Integer size, String sortBy, String sortDirection, Long workoutId) {
        return WorkoutExerciseFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .workoutId(workoutId)
                .build();
    }

    public static WorkoutExerciseUpdateRequest createUpdateRequest(Long id, Integer repetitions, Integer sets, String restDuration) {
        return WorkoutExerciseUpdateRequest.builder()
                .id(id)
                .repetitions(repetitions)
                .sets(sets)
                .restDuration(restDuration)
                .build();
    }

    public static WorkoutExerciseCreateRequest createCreateRequest(Long workoutId, Long exerciseId, Integer repetitions, Integer sets, String restDuration) {
        return WorkoutExerciseCreateRequest.builder()
                .workoutId(workoutId)
                .exerciseId(exerciseId)
                .repetitions(repetitions)
                .sets(sets)
                .restDuration(restDuration)
                .build();
    }

    public static void assertGet(WorkoutExercise workoutExercise, WorkoutExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutExercise.getId(), response.id());
        Assertions.assertEquals(workoutExercise.getWorkout().getId(), response.workoutId());
        Assertions.assertEquals(workoutExercise.getExercise().getName(), response.exerciseName());
        Assertions.assertEquals(workoutExercise.getRepetitions(), response.repetitions());
        Assertions.assertEquals(workoutExercise.getSets(), response.sets());
        Assertions.assertEquals(workoutExercise.getRestDuration().toString(), response.restDuration());
    }

    public static void assertDelete(boolean exists, WorkoutExercise workoutExercise, Long deletedWorkoutExerciseId) {
        assertFalse(exists);
        assertEquals(workoutExercise.getId(), deletedWorkoutExerciseId);
    }

    public static void assertCreate(WorkoutExerciseCreateRequest request, String exerciseName, WorkoutExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(request.workoutId(), response.workoutId());
        Assertions.assertEquals(exerciseName, response.exerciseName());
        Assertions.assertEquals(request.restDuration(), response.restDuration());
        Assertions.assertEquals(request.sets(), response.sets());
        Assertions.assertEquals(request.repetitions(), response.repetitions());
    }

    public static void assertFilter(PageResponse<WorkoutExerciseResponse> response, List<WorkoutExercise> expectedList) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.results());
        Assertions.assertEquals(expectedList.size(), response.results().size());
        List<WorkoutExerciseResponse> sortedResults = response.results().stream().sorted(Comparator.comparing(WorkoutExerciseResponse::id)).toList();
        List<WorkoutExercise> sortedExpectedList = expectedList.stream().sorted(Comparator.comparing(WorkoutExercise::getId)).toList();
        for (int i = 0; i < sortedExpectedList.size(); i++) {
            WorkoutExercise expected = sortedExpectedList.get(i);
            WorkoutExerciseResponse workoutExerciseResponse = sortedResults.get(i);
            assertGet(expected, workoutExerciseResponse);
        }
    }

    public static void assertUpdate(WorkoutExerciseUpdateRequest request, WorkoutExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.id(), response.id());
        Assertions.assertEquals(request.restDuration(), response.restDuration());
        Assertions.assertEquals(request.repetitions(), response.repetitions());
        Assertions.assertEquals(request.sets(), response.sets());
    }
}
