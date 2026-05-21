package sk.krizan.fitness_app_be.domain.workout.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.helper.TagHelper;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkoutHelper {

    public static Workout createWorkout(
            Profile profile,
            Set<Tag> tags,
            List<WorkoutExercise> workoutExercises,
            String title,
            Boolean isTemplate
    ) {
        Workout workout = new Workout();
        workout.setTitle(title);
        workout.setWeightUnit(WeightUnit.KG);
        workout.setNote(UUID.randomUUID().toString());
        workout.addToTags(tags);
        workout.setIsTemplate(isTemplate);

        profile.addToAuthoredWorkouts(workout);

        if (!isTemplate) {
            profile.addToAssignedWorkouts(workout);
        }

        workoutExercises.forEach(workout::addToWorkoutExercises);

        return workout;
    }

    /**
     * Each main list represents one workout. For example, if we need 3 workouts,
     * we should call this method with 3 Profile objects, 3 WorkoutExercise lists and 3 Tag lists.
     */
    public static List<Workout> createWorkoutList(
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
            Workout mockWorkout = createWorkout(profile, tagSet, workoutExercises, UUID.randomUUID().toString(), false);
            if (i == profileList.size() - 1) {
                mockWorkout.setIsTemplate(true);
            }
            workoutExercises.forEach(mockWorkout::addToWorkoutExercises);
            result.add(mockWorkout);
        }

        return result;
    }

    public static WorkoutInputRequest createInputRequest(
            Long traineeId,
            Boolean isTemplate,
            Set<TagCreateRequest> tags,
            List<WorkoutExerciseInputRequest> workoutExercises
    ) {
        return WorkoutInputRequest.builder()
                .traineeId(traineeId)
                .title(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .isTemplate(isTemplate)
                .note(UUID.randomUUID().toString())
                .weightUnit(WeightUnit.KG)
                .tags(tags)
                .workoutExercises(workoutExercises)
                .build();
    }

    public static WorkoutFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            Long authorId,
            List<Long> tagIdList,
            String title,
            Boolean isTemplate) {
        return WorkoutFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .authorId(authorId)
                .tagIdList(tagIdList)
                .title(title)
                .isTemplate(isTemplate)
                .build();
    }

    public static void assertWorkoutDetailResponse(Workout workout, WorkoutDetailResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workout.getId(), response.id());
        ProfileHelper.assertProfileSimpleResponse(workout.getAuthor(), response.author());
        ProfileHelper.assertProfileSimpleResponse(workout.getTrainee(), response.trainee());
        Assertions.assertEquals(workout.getTitle(), response.title());
        Assertions.assertEquals(workout.getDescription(), response.description());
        ReferenceDataHelper.assertReferenceDataResponse(workout.getWeightUnit(), response.weightUnit());
        Assertions.assertEquals(workout.getNote(), response.note());
        Assertions.assertEquals(workout.getIsTemplate(), response.isTemplate());
        assertTagsToTagResponses(workout.getTags(), response.tags());
        assertWorkoutExercises(workout, response);
    }

    private static void assertWorkoutExercises(Workout workout, WorkoutDetailResponse response) {
        List<WorkoutExercise> sortedWorkoutExercises = workout.getWorkoutExercises().stream()
                .sorted(Comparator.comparing(WorkoutExercise::getOrder))
                .toList();
        for (int i = 0; i < sortedWorkoutExercises.size(); i++) {
            WorkoutExercise workoutExercise = sortedWorkoutExercises.get(i);
            WorkoutExerciseResponse workoutExerciseResponse = response.workoutExercises().get(i);
            WorkoutExerciseHelper.assertWorkoutExerciseResponse(workoutExercise, workoutExerciseResponse);
        }
    }

    public static void assertWorkoutSimpleResponse(Workout workout, WorkoutSimpleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workout.getId(), response.id());
        Assertions.assertEquals(workout.getTitle(), response.title());
        ProfileHelper.assertProfileSimpleResponse(workout.getAuthor(), response.author());
        ProfileHelper.assertProfileSimpleResponse(workout.getTrainee(), response.trainee());
        Assertions.assertEquals(workout.getIsTemplate(), response.isTemplate());
        assertTagsToTagResponses(workout.getTags(), response.tags());
    }

    private static void assertTagsToTagResponses(Set<Tag> tags, List<TagResponse> tagResponseList) {
        Assertions.assertEquals(tags.size(), tagResponseList.size());
        List<Tag> sortedTags = tags.stream()
                .sorted(Comparator.comparing(Tag::getId))
                .toList();
        List<TagResponse> sortedTagResponses = tagResponseList.stream()
                .sorted(Comparator.comparing(TagResponse::id))
                .toList();
        for (int i = 0; i < sortedTags.size(); i++) {
            TagHelper.assertResponse(sortedTags.get(i), sortedTagResponses.get(i));
        }
    }

    public static void assertInputToEntity(Workout workout, WorkoutInputRequest workoutInputRequest) {
        Assertions.assertNotNull(workout);
        Assertions.assertNotNull(workout.getId());
        Assertions.assertEquals(workoutInputRequest.title(), workout.getTitle());
        Assertions.assertEquals(workoutInputRequest.description(), workout.getDescription());
        Assertions.assertEquals(workoutInputRequest.isTemplate(), workout.getIsTemplate());
        Assertions.assertEquals(workoutInputRequest.note(), workout.getNote());
        Assertions.assertEquals(workoutInputRequest.weightUnit(), workout.getWeightUnit());

        if (!workoutInputRequest.isTemplate()) {
            Assertions.assertEquals(workoutInputRequest.traineeId(), workout.getTrainee().getId());
        }

        assertTagCreateRequestsToTags(workout.getTags(), workoutInputRequest.tags());
        assertWorkoutExerciseInputRequestsToWorkoutExercises(workout, workoutInputRequest);
    }

    private static void assertWorkoutExerciseInputRequestsToWorkoutExercises(Workout workout, WorkoutInputRequest workoutInputRequest) {
        Assertions.assertEquals(workoutInputRequest.workoutExercises().size(), workout.getWorkoutExercises().size());
        List<WorkoutExercise> sortedWorkoutExercises = workout.getWorkoutExercises().stream()
                .sorted(Comparator.comparing(WorkoutExercise::getOrder))
                .toList();
        List<WorkoutExerciseInputRequest> sortedWorkoutExerciseInputRequests = workoutInputRequest.workoutExercises().stream()
                .sorted(Comparator.comparing(WorkoutExerciseInputRequest::order))
                .toList();

        for (int i = 0; i < sortedWorkoutExercises.size(); i++) {
            WorkoutExercise workoutExercise = sortedWorkoutExercises.get(i);
            WorkoutExerciseInputRequest workoutExerciseInputRequest = sortedWorkoutExerciseInputRequests.get(i);
            WorkoutExerciseHelper.assertInputToEntity(workoutExercise, workoutExerciseInputRequest);
        }
    }

    private static void assertTagCreateRequestsToTags(Set<Tag> tags, Set<TagCreateRequest> tagCreateRequests) {
        Assertions.assertEquals(tagCreateRequests.size(), tags.size());
        List<Tag> sortedTags = tags.stream()
                .sorted(Comparator.comparing(Tag::getName))
                .toList();
        List<TagCreateRequest> sortedTagCreateRequests = tagCreateRequests.stream()
                .sorted(Comparator.comparing(TagCreateRequest::name))
                .toList();

        for (int i = 0; i < sortedTags.size(); i++) {
            Tag tag = sortedTags.get(i);
            TagCreateRequest tagCreateRequest = sortedTagCreateRequests.get(i);
            TagHelper.assertCreateRequestToEntity(tag, tagCreateRequest);
        }
    }

    public static void assertClone(Workout original, Workout clone) {
        Assertions.assertNotNull(clone);
        Assertions.assertNotNull(clone.getId());
        Assertions.assertNotEquals(original.getId(), clone.getId());
        Assertions.assertEquals(original.getTitle(), clone.getTitle());
        Assertions.assertEquals(original.getDescription(), clone.getDescription());
        Assertions.assertFalse(clone.getIsTemplate());
        Assertions.assertNull(clone.getNote());
        Assertions.assertEquals(original.getWeightUnit(), clone.getWeightUnit());
        Assertions.assertEquals(original.getTags(), clone.getTags());
        Assertions.assertNotNull(clone.getAuthor());

        //  trainee is asserted in WeekWorkoutHelper.assertInputToEntity, because workout can be cloned only when creating or updating week workout, so we need to assert that the trainee of the cloned workout is the same as the trainee of the week workout.

        assertCloneWorkoutExercises(original.getWorkoutExercises(), clone.getWorkoutExercises());
    }

    private static void assertCloneWorkoutExercises(List<WorkoutExercise> originalWorkoutExercises, List<WorkoutExercise> cloneWorkoutExercises) {
        Assertions.assertEquals(originalWorkoutExercises.size(), cloneWorkoutExercises.size());
        List<WorkoutExercise> sortedOriginalWorkoutExercises = originalWorkoutExercises.stream()
                .sorted(Comparator.comparing(WorkoutExercise::getOrder))
                .toList();
        List<WorkoutExercise> sortedCloneWorkoutExercises = cloneWorkoutExercises.stream()
                .sorted(Comparator.comparing(WorkoutExercise::getOrder))
                .toList();

        for (int i = 0; i < sortedOriginalWorkoutExercises.size(); i++) {
            WorkoutExercise originalWorkoutExercise = sortedOriginalWorkoutExercises.get(i);
            WorkoutExercise cloneWorkoutExercise = sortedCloneWorkoutExercises.get(i);
            WorkoutExerciseHelper.assertClone(originalWorkoutExercise, cloneWorkoutExercise);
        }
    }

    public static void assertDelete(
            boolean workoutExists,
            boolean workoutExerciseExists,
            boolean workoutExerciseSetExists
    ) {
        Assertions.assertFalse(workoutExists);
        Assertions.assertFalse(workoutExerciseExists);
        Assertions.assertFalse(workoutExerciseSetExists);
    }
}
