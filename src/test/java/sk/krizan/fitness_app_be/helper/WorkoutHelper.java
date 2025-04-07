package sk.krizan.fitness_app_be.helper;

import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_UPDATE_VALUE;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

public class WorkoutHelper {

    public static WorkoutFilterRequest createFilterRequest(Integer page, Integer size, String sortBy, String sortDirection) {
        return WorkoutFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
    }

    public static Workout createMockWorkout(
            Profile profile,
            List<WorkoutExercise> workoutExerciseList,
            Set<Tag> tagSet
    ) {
        Workout workout = new Workout();
        workout.setName(DEFAULT_VALUE);
        workout.addToTagSet(tagSet);
        workout.addToWorkoutExerciseList(workoutExerciseList);

        profile.addToAuthoredWorkoutList(List.of(workout));
        return workout;
    }

    /**
     * Each main list represents one workout. For example, if we need 3 workouts,
     * we should call this method with 3 Profile objects, 3 WorkoutExercise lists and 3 Tag lists.
     */
    public static List<Workout> createMockWorkoutList(
            List<Profile> profileList,
            List<List<WorkoutExercise>> workoutExerciseList,
            List<Set<Tag>> tagList
    ) throws Exception {
        if (profileList.size() != workoutExerciseList.size() && workoutExerciseList.size() != tagList.size()) {
            throw new Exception("Collections are not of the same size.");
        }

        List<Workout> result = new ArrayList<>();

        for (int i = 0; i < profileList.size(); i++) {
            Profile profile = profileList.get(i);
            List<WorkoutExercise> workoutExercises = workoutExerciseList.get(i);
            Set<Tag> tagSet = tagList.get(i);
            Workout mockWorkout = createMockWorkout(profile, workoutExercises, tagSet);
            result.add(mockWorkout);
        }

        return result;
    }

    public static WorkoutCreateRequest createCreateRequest() {
        return WorkoutCreateRequest.builder()
                .name(DEFAULT_VALUE)
                .build();
    }

    public static WorkoutUpdateRequest createUpdateRequest() {
        return WorkoutUpdateRequest.builder()
                .name(DEFAULT_UPDATE_VALUE)
                .description(DEFAULT_UPDATE_VALUE)
                .tagNames(Set.of(DEFAULT_UPDATE_VALUE))
                .build();
    }

    public static void assertWorkoutResponse_get(Workout workout, WorkoutResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workout.getId(), response.id());
        Assertions.assertEquals(DEFAULT_VALUE, response.name());
        Assertions.assertTrue(response.tagResponseList().isEmpty());
    }

    public static void assertWorkoutResponse_create(
            WorkoutCreateRequest request,
            String authorName,
            WorkoutResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(authorName, response.authorName());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNull(response.description());
        Assertions.assertTrue(response.tagResponseList().isEmpty());
    }

    public static void assertWorkoutResponse_update(
            WorkoutUpdateRequest request,
            String authorName,
            WorkoutResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(authorName, response.authorName());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNotNull(response.description());

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
}
