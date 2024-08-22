package sk.krizan.fitness_app_be.helper;

import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.TagResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.Level;

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
            String name,
            Profile profile,
            List<WorkoutExercise> workoutExerciseList,
            Set<Tag> tagList
    ) {
        Workout workout = Workout.builder()
                .author(profile)
                .name(name)
                .tags(tagList)
                .workoutExerciseList(workoutExerciseList)
                .build();
        profile.getAuthoredWorkouts().add(workout);
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
            Set<Tag> tags = tagList.get(i);
            Workout mockWorkout = createMockWorkout(DEFAULT_VALUE, profile, workoutExercises, tags);
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
                .levelKey(Level.ADVANCED.getKey())
                .tagNames(Set.of(DEFAULT_UPDATE_VALUE))
                .build();
    }

    public static void assertWorkoutSimpleResponse(WorkoutSimpleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(DEFAULT_VALUE, response.name());
        Assertions.assertTrue(response.tagResponseList().isEmpty());
    }

    public static void assertWorkoutDetailResponse_get() {

    }

    public static void assertWorkoutDetailResponse_create(
            WorkoutCreateRequest request,
            String authorName,
            WorkoutDetailResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.authorName());
        Assertions.assertEquals(authorName, response.authorName());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNull(response.levelValue());
        Assertions.assertNull(response.description());
        Assertions.assertTrue(response.tagResponseList().isEmpty());
        Assertions.assertTrue(response.workoutExerciseSimpleResponseList().isEmpty());
    }

    public static void assertWorkoutDetailResponse_update(
            WorkoutUpdateRequest request,
            String authorName,
            WorkoutDetailResponse response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.authorName());
        Assertions.assertEquals(authorName, response.authorName());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(request.name(), response.name());
        Assertions.assertNotNull(response.levelValue());
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

        Assertions.assertTrue(response.workoutExerciseSimpleResponseList().isEmpty());
    }

    public static void assertDelete(boolean exists, Workout savedMockWorkout, Long deletedWorkoutId) {
        assertFalse(exists);
        assertEquals(savedMockWorkout.getId(), deletedWorkoutId);
    }
}
