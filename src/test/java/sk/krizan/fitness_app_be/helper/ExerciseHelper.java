package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExerciseHelper {

    public static Exercise createMockExercise(String name, Set<MuscleGroup> muscleGroups) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.addToMuscleGroupSet(muscleGroups);
        return exercise;
    }

    public static ExerciseFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            String name,
            List<MuscleGroup> muscleGroupList
    ) {
        return ExerciseFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .name(name)
                .muscleGroupKeyList(
                        muscleGroupList != null
                                ? muscleGroupList.stream().map(MuscleGroup::getKey).collect(Collectors.toList())
                                : new ArrayList<>())
                .build();
    }

    public static ExerciseCreateRequest createCreateRequest(
            @NotEmpty @Size String name,
            @NotNull Set<String> muscleGroupKeys
    ) {
        return ExerciseCreateRequest.builder()
                .name(name)
                .muscleGroupKeys(muscleGroupKeys)
                .build();
    }

    public static void assertExerciseResponse_create(ExerciseCreateRequest request, ExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNotNull(response.muscleGroupValues());
        Assertions.assertFalse(response.muscleGroupValues().isEmpty());
        Assertions.assertEquals(request.muscleGroupKeys().size(), response.muscleGroupValues().size());
        List<String> requestMuscleGroupValueList = request.muscleGroupKeys().stream()
                .map(MuscleGroup::valueOf)
                .map(MuscleGroup::getValue)
                .toList();
        Assertions.assertTrue(response.muscleGroupValues().containsAll(requestMuscleGroupValueList));
    }

    public static void assertDelete(boolean exists, Exercise savedExercise, Long deletedExerciseId) {
        assertFalse(exists);
        assertEquals(savedExercise.getId(), deletedExerciseId);
    }

    public static void assertFilter(
            List<Exercise> exerciseList,
            ExerciseFilterRequest request,
            PageResponse<ExerciseResponse> response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.pageNumber());
        Assertions.assertNotNull(response.pageSize());
        Assertions.assertNotNull(response.totalElements());
        Assertions.assertNotNull(response.totalPages());
        Assertions.assertNotNull(response.results());
        Assertions.assertFalse(response.results().isEmpty());
        Assertions.assertEquals(request.page(), response.pageNumber());
        Assertions.assertEquals(request.size(), response.results().size());

        List<ExerciseResponse> results = response.results();
        results.sort(Comparator.comparingLong(ExerciseResponse::id));
        exerciseList.sort(Comparator.comparingLong(Exercise::getId));
        for (int i = 0; i < results.size(); i++) {
            ExerciseResponse exerciseResponse = results.get(i);
            Exercise exercise = exerciseList.get(i);
            assertExerciseResponse(exerciseResponse, exercise);
        }
    }

    private static void assertExerciseResponse(ExerciseResponse exerciseResponse, Exercise exercise) {
        Assertions.assertNotNull(exerciseResponse);
        Assertions.assertNotNull(exerciseResponse.id());
        Assertions.assertEquals(exercise.getId(), exerciseResponse.id());
        Assertions.assertNotNull(exerciseResponse.name());
        Assertions.assertEquals(exercise.getName(), exerciseResponse.name());
        Assertions.assertNotNull(exerciseResponse.muscleGroupValues());
        Assertions.assertFalse(exerciseResponse.muscleGroupValues().isEmpty());
        Assertions.assertEquals(exercise.getMuscleGroupSet().size(), exerciseResponse.muscleGroupValues().size());
        boolean containsAllMuscleGroupValues = exerciseResponse.muscleGroupValues().containsAll(
                exercise.getMuscleGroupSet().stream().map(MuscleGroup::getValue).toList());
        Assertions.assertTrue(containsAllMuscleGroupValues);
    }

    /**
     * Contains:
     * <ul>
     * <li>name: 'Bench press', muscle groups: [CHEST, SHOULDERS, TRICEPS]</li>
     * <li>name: 'Push ups', muscle groups: [CHEST, SHOULDERS, TRICEPS]</li>
     * <li>name: 'Pull ups', muscle groups: [BACK, BICEPS]</li>
     * <li>name: 'Biceps curls', muscle groups: [BICEPS]</li>
     * <li>name: 'Squats', muscle groups: [LEGS]</li>
     * </ul>
     * <br>
     * @return List of sample exercises
     */
    public static List<Exercise> createOriginalExercises() {
        Exercise benchPress = ExerciseHelper.createMockExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        Exercise pushUps = ExerciseHelper.createMockExercise("Push ups", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        Exercise pullUps = ExerciseHelper.createMockExercise("Pull ups", Set.of(MuscleGroup.BACK, MuscleGroup.BICEPS));
        Exercise bicepsCurls = ExerciseHelper.createMockExercise("Biceps curls", Set.of(MuscleGroup.BICEPS));
        Exercise squats = ExerciseHelper.createMockExercise("Squats", Set.of(MuscleGroup.LEGS));

        return List.of(benchPress, pushUps, pullUps, bicepsCurls, squats);
    }

    public static void assertExerciseResponse_get(Exercise exercise, ExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(exercise.getId(), response.id());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(exercise.getName(), response.name());
        Assertions.assertNotNull(response.muscleGroupValues());
        List<String> requestMuscleGroupValueList = exercise.getMuscleGroupSet().stream()
                .map(MuscleGroup::getValue).toList();
        Assertions.assertTrue(response.muscleGroupValues().containsAll(requestMuscleGroupValueList));
    }
}
