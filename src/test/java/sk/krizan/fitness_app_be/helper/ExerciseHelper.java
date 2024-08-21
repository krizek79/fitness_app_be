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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExerciseHelper {

    public static Exercise createMockExercise(String name, Set<MuscleGroup> muscleGroups) {
        return Exercise.builder()
                .name(name)
                .muscleGroups(muscleGroups)
                .build();
    }

    public static ExerciseFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            String name,
            List<String> muscleGroupList
    ) {
        return ExerciseFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .name(name)
                .muscleGroupKeyList(muscleGroupList)
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

    public static void assertFilter(List<Exercise> exerciseList, ExerciseFilterRequest request, PageResponse<ExerciseResponse> response) {
        Assertions.assertNotNull(response);
    }

    public static List<Exercise> createOriginalExercises() {
        Exercise benchPress = ExerciseHelper.createMockExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        Exercise pushUps = ExerciseHelper.createMockExercise("Push ups", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        Exercise pullUps = ExerciseHelper.createMockExercise("Pull ups", Set.of(MuscleGroup.BACK, MuscleGroup.BICEPS));
        Exercise squats = ExerciseHelper.createMockExercise("Squats", Set.of(MuscleGroup.LEGS));

        return List.of(benchPress, pushUps, pullUps, squats);
    }
}
