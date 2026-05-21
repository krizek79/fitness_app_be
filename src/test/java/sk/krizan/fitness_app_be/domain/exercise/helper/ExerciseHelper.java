package sk.krizan.fitness_app_be.domain.exercise.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseResponse;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExerciseHelper {

    public static Exercise createExercise(String name, Set<MuscleGroup> muscleGroups) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.addToMuscleGroups(muscleGroups);
        return exercise;
    }

    public static ExerciseFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            String name,
            List<MuscleGroup> muscleGroupList
    ) {
        return ExerciseFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .name(name)
                .muscleGroupList(muscleGroupList)
                .build();
    }

    public static ExerciseCreateRequest createCreateRequest(
            String name,
            Set<MuscleGroup> muscleGroupSet
    ) {
        return ExerciseCreateRequest.builder()
                .name(name)
                .muscleGroupSet(muscleGroupSet)
                .build();
    }

    public static void assertExerciseResponse_create(ExerciseCreateRequest request, ExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNotNull(response.muscleGroupResponseList());
        Assertions.assertFalse(response.muscleGroupResponseList().isEmpty());
        Assertions.assertEquals(request.muscleGroupSet().size(), response.muscleGroupResponseList().size());
        Set<String> responseKeys = response.muscleGroupResponseList()
                .stream()
                .map(ReferenceDataResponse::key)
                .collect(Collectors.toSet());
        Set<String> expectedKeys = request.muscleGroupSet()
                .stream()
                .map(MuscleGroup::getKey)
                .collect(Collectors.toSet());
        Assertions.assertEquals(expectedKeys, responseKeys);
    }

    /**
     * Contains:
     * <ul>
     * <li>title: 'Bench press', muscle groups: [CHEST, SHOULDERS, TRICEPS]</li>
     * <li>title: 'Push ups', muscle groups: [CHEST, SHOULDERS, TRICEPS]</li>
     * <li>title: 'Pull ups', muscle groups: [BACK, BICEPS]</li>
     * <li>title: 'Biceps curls', muscle groups: [BICEPS]</li>
     * <li>title: 'Squats', muscle groups: [LEGS]</li>
     * </ul>
     * <br>
     *
     * @return List of sample exercises
     */
    public static List<Exercise> createOriginalExercises() {
        Exercise benchPress = ExerciseHelper.createExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        Exercise pushUps = ExerciseHelper.createExercise("Push ups", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        Exercise pullUps = ExerciseHelper.createExercise("Pull ups", Set.of(MuscleGroup.BACK, MuscleGroup.BICEPS));
        Exercise bicepsCurls = ExerciseHelper.createExercise("Biceps curls", Set.of(MuscleGroup.BICEPS));
        Exercise squats = ExerciseHelper.createExercise("Squats", Set.of(MuscleGroup.LEGS));

        return List.of(benchPress, pushUps, pullUps, bicepsCurls, squats);
    }

    public static void assertExerciseResponse(Exercise exercise, ExerciseResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(exercise.getId(), response.id());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(exercise.getName(), response.name());
        Assertions.assertNotNull(response.muscleGroupResponseList());
        Assertions.assertFalse(response.muscleGroupResponseList().isEmpty());
        Assertions.assertEquals(exercise.getMuscleGroups().size(), response.muscleGroupResponseList().size());
        Set<String> expectedKeys = exercise.getMuscleGroups()
                .stream()
                .map(MuscleGroup::getKey)
                .collect(Collectors.toSet());
        Set<String> expectedValues = exercise.getMuscleGroups()
                .stream()
                .map(MuscleGroup::getValue)
                .collect(Collectors.toSet());
        Set<String> responseKeys = response.muscleGroupResponseList()
                .stream()
                .map(ReferenceDataResponse::key)
                .collect(Collectors.toSet());
        Set<String> responseValues = response.muscleGroupResponseList()
                .stream()
                .map(ReferenceDataResponse::value)
                .collect(Collectors.toSet());

        Assertions.assertEquals(expectedKeys, responseKeys);
        Assertions.assertEquals(expectedValues, responseValues);
    }

}
