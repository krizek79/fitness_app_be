package sk.krizan.fitness_app_be.domain.exercise.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.helper.EquipmentHelper;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.entity.ExerciseCategory;
import sk.krizan.fitness_app_be.domain.exercise.entity.MovementPattern;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseDetailResponse;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseSimpleResponse;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRole;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRoleType;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.helper.ExerciseMuscleRoleHelper;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.response.ExerciseMuscleRoleResponse;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExerciseHelper {

    public static Exercise createExercise(
            String title,
            ExerciseCategory exerciseCategory,
            List<MovementPattern> movementPatterns,
            List<ExerciseMuscleRole> muscles,
            List<Equipment> equipment
    ) {
        Exercise exercise = new Exercise();
        exercise.setTitle(title);
        exercise.setExerciseCategory(exerciseCategory);
        exercise.addToMovementPatterns(new HashSet<>(movementPatterns));
        muscles.forEach(exercise::addToMuscles);
        equipment.forEach(exercise::addToRequiredEquipment);

        return exercise;
    }

    public static ExerciseFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            String title,
            ExerciseCategory exerciseCategory,
            List<MovementPattern> movementPatterns,
            List<Muscle> muscles,
            List<Long> requiredEquipmentIds
    ) {
        return ExerciseFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .title(title)
                .exerciseCategory(exerciseCategory)
                .movementPatterns(movementPatterns)
                .muscles(muscles)
                .requiredEquipmentIds(requiredEquipmentIds)
                .build();
    }

    public static ExerciseInputRequest createInputRequest(
            String title,
            ExerciseCategory exerciseCategory,
            Set<MovementPattern> movementPatterns,
            List<ExerciseMuscleRoleInputRequest> muscles,
            List<Long> equipmentIds
    ) {
        return ExerciseInputRequest.builder()
                .title(title)
                .exerciseCategory(exerciseCategory)
                .muscles(muscles)
                .movementPatterns(movementPatterns)
                .requiredEquipmentIds(equipmentIds)
                .build();
    }

    /**
     * @return List of 6 sample exercises
     */
    public static List<Exercise> createOriginalExercises(List<Equipment> equipment) {
        Exercise benchPress = ExerciseHelper.createExercise("Bench press", ExerciseCategory.STRENGTH, List.of(MovementPattern.HORIZONTAL_PUSH), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.CHEST, ExerciseMuscleRoleType.PRIMARY), ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.SHOULDERS, ExerciseMuscleRoleType.SECONDARY)), List.of(equipment.iterator().next()));
        Exercise pushUps = ExerciseHelper.createExercise("Push ups", ExerciseCategory.STRENGTH, List.of(MovementPattern.HORIZONTAL_PUSH), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.CHEST, ExerciseMuscleRoleType.PRIMARY)), List.of(equipment.iterator().next()));
        Exercise pullUps = ExerciseHelper.createExercise("Pull ups", ExerciseCategory.STRENGTH, List.of(MovementPattern.VERTICAL_PULL), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.BACK, ExerciseMuscleRoleType.PRIMARY)), List.of(equipment.iterator().next()));
        Exercise bicepsCurls = ExerciseHelper.createExercise("Biceps curls", ExerciseCategory.STRENGTH, List.of(), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.BICEPS, ExerciseMuscleRoleType.PRIMARY)), List.of(equipment.iterator().next()));
        Exercise squats = ExerciseHelper.createExercise("Squats", ExerciseCategory.STRENGTH, List.of(MovementPattern.SQUAT), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.QUADS, ExerciseMuscleRoleType.PRIMARY)), List.of(equipment.iterator().next()));
        Exercise sprint = ExerciseHelper.createExercise("Sprint", ExerciseCategory.CARDIO, List.of(MovementPattern.LOCOMOTION), List.of(), List.of());

        return List.of(benchPress, pushUps, pullUps, bicepsCurls, squats, sprint);
    }

    public static void assertExerciseDetailResponse(Exercise exercise, ExerciseDetailResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.title());
        ReferenceDataHelper.assertReferenceDataResponse(exercise.getExerciseCategory(), response.exerciseCategory());
        if (exercise.getThumbnailUrl() != null) {
            Assertions.assertEquals(exercise.getThumbnailUrl(), response.thumbnailUrl());
        } else {
            Assertions.assertNull(response.thumbnailUrl());
        }

        Assertions.assertEquals(exercise.getMovementPatterns().size(), response.movementPatterns().size());
        List<MovementPattern> sortedExerciseMovementPatterns = exercise.getMovementPatterns().stream().sorted(Comparator.comparing(BaseEnum::getKey)).toList();
        List<ReferenceDataResponse> sortedResponseMovementPatterns = response.movementPatterns().stream().sorted(Comparator.comparing(ReferenceDataResponse::key)).toList();
        for (int i = 0; i < sortedExerciseMovementPatterns.size(); i++) {
            MovementPattern movementPattern = sortedExerciseMovementPatterns.get(i);
            ReferenceDataResponse referenceDataResponse = sortedResponseMovementPatterns.get(i);
            ReferenceDataHelper.assertReferenceDataResponse(movementPattern, referenceDataResponse);
        }

        Assertions.assertEquals(exercise.getMuscles().size(), response.muscles().size());
        List<ExerciseMuscleRole> sortedExerciseMuscles = exercise.getMuscles().stream().sorted(Comparator.comparing(ExerciseMuscleRole::getId)).toList();
        List<ExerciseMuscleRoleResponse> sortedResponseMuscles = response.muscles().stream().sorted(Comparator.comparing(ExerciseMuscleRoleResponse::id)).toList();
        for (int i = 0; i < sortedExerciseMuscles.size(); i++) {
            ExerciseMuscleRole exerciseMuscle = sortedExerciseMuscles.get(i);
            ExerciseMuscleRoleResponse responseMuscle = sortedResponseMuscles.get(i);
            ExerciseMuscleRoleHelper.assertExerciseMuscleRoleResponse(exerciseMuscle, responseMuscle);
        }

        Assertions.assertEquals(exercise.getRequiredEquipment().size(), response.requiredEquipment().size());
        List<Equipment> sortedExerciseEquipment = exercise.getRequiredEquipment().stream().sorted(Comparator.comparing(Equipment::getId)).toList();
        List<EquipmentResponse> sortedResponseEquipment = response.requiredEquipment().stream().sorted(Comparator.comparing(EquipmentResponse::id)).toList();
        for (int i = 0; i < sortedExerciseEquipment.size(); i++) {
            Equipment equipment = sortedExerciseEquipment.get(i);
            EquipmentResponse responseEquipment = sortedResponseEquipment.get(i);
            EquipmentHelper.assertEquipmentResponse(equipment, responseEquipment);
        }
    }

    public static void assertExerciseSimpleResponse(Exercise exercise, ExerciseSimpleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(exercise.getId(), response.id());
        Assertions.assertEquals(exercise.getTitle(), response.title());
        Assertions.assertEquals(exercise.getThumbnailUrl(), response.thumbnailUrl());
        ReferenceDataHelper.assertReferenceDataResponse(exercise.getExerciseCategory(), response.exerciseCategory());

        List<ExerciseMuscleRole> originalPrimaryMuscles = exercise.getMuscles().stream().filter(muscle -> ExerciseMuscleRoleType.PRIMARY.equals(muscle.getType())).sorted(Comparator.comparing(muscle -> muscle.getMuscle().getKey())).toList();
        Assertions.assertEquals(originalPrimaryMuscles.size(), response.primaryMuscles().size());
        List<ReferenceDataResponse> sortedResponsePrimaryMuscles = response.primaryMuscles().stream().sorted(Comparator.comparing(ReferenceDataResponse::key)).toList();
        for (int i = 0; i < originalPrimaryMuscles.size(); i++) {
            Muscle originalMuscle = originalPrimaryMuscles.get(i).getMuscle();
            ReferenceDataResponse responseMuscle = sortedResponsePrimaryMuscles.get(i);
            ReferenceDataHelper.assertReferenceDataResponse(originalMuscle, responseMuscle);
        }
    }

    public static void assertInputToEntity(ExerciseInputRequest request, Exercise exercise) {
        Assertions.assertNotNull(exercise);
        Assertions.assertEquals(request.title(), exercise.getTitle());
        Assertions.assertEquals(request.exerciseCategory(), exercise.getExerciseCategory());
        Assertions.assertEquals(request.movementPatterns().size(), exercise.getMovementPatterns().size());
        List<MovementPattern> sortedRequestMovementPatterns = request.movementPatterns().stream().sorted(Comparator.comparing(BaseEnum::getKey)).toList();
        List<MovementPattern> sortedExerciseMovementPatterns = exercise.getMovementPatterns().stream().sorted(Comparator.comparing(BaseEnum::getKey)).toList();
        for (int i = 0; i < sortedRequestMovementPatterns.size(); i++) {
            MovementPattern requestMovementPattern = sortedRequestMovementPatterns.get(i);
            MovementPattern exerciseMovementPattern = sortedExerciseMovementPatterns.get(i);
            Assertions.assertEquals(requestMovementPattern, exerciseMovementPattern);
        }

        Assertions.assertEquals(request.muscles().size(), exercise.getMuscles().size());
        List<ExerciseMuscleRoleInputRequest> sortedRequestMuscles = request.muscles().stream().sorted(Comparator.comparing(ExerciseMuscleRoleInputRequest::id, Comparator.nullsLast(Comparator.naturalOrder()))).toList();
        List<ExerciseMuscleRole> sortedExerciseMuscles = exercise.getMuscles().stream().sorted(Comparator.comparing(ExerciseMuscleRole::getId)).toList();
        for (int i = 0; i < sortedRequestMuscles.size(); i++) {
            ExerciseMuscleRoleInputRequest requestMuscle = sortedRequestMuscles.get(i);
            ExerciseMuscleRole exerciseMuscle = sortedExerciseMuscles.get(i);
            ExerciseMuscleRoleHelper.assertInputRequest(exerciseMuscle, requestMuscle);
        }

        Assertions.assertEquals(request.requiredEquipmentIds().size(), exercise.getRequiredEquipment().size());
        List<Long> sortedRequestEquipmentIds = request.requiredEquipmentIds().stream().sorted().toList();
        List<Equipment> sortedExerciseEquipment = exercise.getRequiredEquipment().stream().sorted(Comparator.comparing(Equipment::getId)).toList();
        for (int i = 0; i < sortedRequestEquipmentIds.size(); i++) {
            Long requestEquipmentId = sortedRequestEquipmentIds.get(i);
            Equipment exerciseEquipment = sortedExerciseEquipment.get(i);
            Assertions.assertEquals(requestEquipmentId, exerciseEquipment.getId());
        }
    }

}
