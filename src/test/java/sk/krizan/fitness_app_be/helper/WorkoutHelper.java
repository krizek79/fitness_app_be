package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_UPDATE_VALUE;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutHelper {

    public static WorkoutFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            Long authorId,
            List<Long> tagIdList,
            String name,
            Boolean isTemplate) {
        return WorkoutFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .authorId(authorId)
                .tagIdList(tagIdList)
                .name(name)
                .isTemplate(isTemplate)
                .build();
    }

    public static Workout createMockWorkout(
            Profile profile,
            Set<Tag> tagSet,
            String name
    ) {
        Workout workout = new Workout();
        workout.setName(name);
        workout.setWeightUnit(WeightUnit.KG);
        workout.setNote(UUID.randomUUID().toString());
        workout.addToTagSet(tagSet);

        profile.addToAuthoredWorkoutList(List.of(workout));
        profile.addToAssignedWorkoutList(List.of(workout));

        return workout;
    }

    /**
     * Each main list represents one workout. For example, if we need 3 workouts,
     * we should call this method with 3 Profile objects, 3 WorkoutExercise lists and 3 Tag lists.
     */
    public static List<Workout> createMockWorkoutList(
            List<Profile> profileList,
            List<List<WorkoutExercise>> workoutExerciseList,
            List<Set<Tag>> tagSetList
    ) throws Exception {
        if (profileList.size() != workoutExerciseList.size() || profileList.size() != tagSetList.size()) {
            throw new Exception("Collections must have the same size.");
        }

        List<Workout> result = new ArrayList<>();

        for (int i = 0; i < profileList.size(); i++) {
            Profile profile = profileList.get(i);
            List<WorkoutExercise> workoutExercises = workoutExerciseList.get(i);
            Set<Tag> tagSet = tagSetList.get(i);
            String name = UUID.randomUUID().toString();
            Workout mockWorkout = createMockWorkout(profile, tagSet, name);
            if (i == profileList.size() - 1) {
                mockWorkout.setIsTemplate(true);
            }
            mockWorkout.addToWorkoutExerciseList(workoutExercises);
            result.add(mockWorkout);
        }

        return result;
    }

    public static WorkoutCreateRequest createCreateRequest() {
        return WorkoutCreateRequest.builder()
                .name(DEFAULT_VALUE)
                .isTemplate(true)
                .weightUnitKey(WeightUnit.KG.getKey())
                .note(UUID.randomUUID().toString())
                .build();
    }

    public static WorkoutUpdateRequest createUpdateRequest() {
        return WorkoutUpdateRequest.builder()
                .name(DEFAULT_UPDATE_VALUE)
                .description(DEFAULT_UPDATE_VALUE)
                .note(UUID.randomUUID().toString())
                .weightUnitKey(WeightUnit.LB.getKey())
                .tagNames(Set.of(DEFAULT_UPDATE_VALUE))
                .build();
    }

    public static void assertWorkoutResponse_get(Workout workout, WorkoutResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workout.getId(), response.id());
        Assertions.assertEquals(workout.getAuthor().getId(), response.authorId());
        Assertions.assertEquals(workout.getAuthor().getName(), response.authorName());
        Assertions.assertEquals(workout.getTrainee().getId(), response.traineeId());
        Assertions.assertEquals(workout.getTrainee().getName(), response.traineeName());
        EnumHelper.assertEnumResponse(workout.getWeightUnit().getKey(), response.weightUnitResponse());
        Assertions.assertEquals(workout.getIsTemplate(), response.isTemplate());
        Assertions.assertEquals(workout.getNote(), response.note());
        Assertions.assertTrue(response.tagResponseList().isEmpty());
    }

    public static void assertWorkoutResponse_create(
            WorkoutCreateRequest request,
            Profile profile,
            WorkoutResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(profile.getId(), response.authorId());
        Assertions.assertEquals(profile.getName(), response.authorName());
        Assertions.assertEquals(profile.getId(), response.traineeId());
        Assertions.assertEquals(profile.getName(), response.traineeName());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNull(response.description());
        Assertions.assertTrue(response.tagResponseList().isEmpty());
        Assertions.assertFalse(profile.getAuthoredWorkoutList().isEmpty());
        Assertions.assertEquals(request.isTemplate(), response.isTemplate());
        Assertions.assertEquals(request.note(), response.note());
        EnumHelper.assertEnumResponse(request.weightUnitKey(), response.weightUnitResponse());
    }

    public static void assertWorkoutResponse_update(
            WorkoutUpdateRequest request,
            Profile profile,
            WorkoutResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(profile.getId(), response.authorId());
        Assertions.assertEquals(profile.getName(), response.authorName());
        Assertions.assertEquals(profile.getId(), response.traineeId());
        Assertions.assertEquals(profile.getName(), response.traineeName());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNotNull(response.description());
        Assertions.assertFalse(response.isTemplate());
        Assertions.assertEquals(request.note(), response.note());
        EnumHelper.assertEnumResponse(request.weightUnitKey(), response.weightUnitResponse());

        List<TagResponse> tagResponseList = response.tagResponseList();
        Assertions.assertFalse(tagResponseList.isEmpty());
        Set<String> tagResponseListNames = tagResponseList.stream()
                .map(tagResponse -> tagResponse.name().toLowerCase())
                .collect(Collectors.toSet());
        Set<String> tagNames = request.tagNames().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        Assertions.assertEquals(tagNames, tagResponseListNames);
    }

    public static void assertDelete(boolean exists, Workout savedMockWorkout, Long deletedWorkoutId) {
        assertFalse(exists);
        assertEquals(savedMockWorkout.getId(), deletedWorkoutId);
    }

    public static void assertFilter(PageResponse<WorkoutResponse> response, Workout expectedWorkout) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.results());
        Assertions.assertEquals(1, response.results().size());
        WorkoutResponse workoutResponse = response.results().get(0);
        Assertions.assertNotNull(workoutResponse);
        Assertions.assertEquals(expectedWorkout.getId(), workoutResponse.id());
        Assertions.assertEquals(expectedWorkout.getName(), workoutResponse.name());
        Assertions.assertEquals(expectedWorkout.getDescription(), workoutResponse.description());
        Assertions.assertEquals(expectedWorkout.getAuthor().getId(), workoutResponse.authorId());
        Assertions.assertEquals(expectedWorkout.getAuthor().getName(), workoutResponse.authorName());
        Assertions.assertEquals(expectedWorkout.getTrainee().getId(), workoutResponse.traineeId());
        Assertions.assertEquals(expectedWorkout.getTrainee().getName(), workoutResponse.traineeName());
        Assertions.assertEquals(expectedWorkout.getIsTemplate(), workoutResponse.isTemplate());
        Assertions.assertEquals(expectedWorkout.getNote(), workoutResponse.note());
        EnumHelper.assertEnumResponse(expectedWorkout.getWeightUnit().getKey(), workoutResponse.weightUnitResponse());
        Assertions.assertEquals(expectedWorkout.getTagSet().stream().map(Tag::getId).toList(), workoutResponse.tagResponseList().stream().map(TagResponse::id).toList());
    }
}
