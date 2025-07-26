package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseHelper {

    public static WorkoutExercise createMockWorkoutExercise(Workout workout, Exercise exercise, Integer order) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);
        workoutExercise.setOrder(order);
        workoutExercise.setNote(UUID.randomUUID().toString());
        workoutExercise.setWorkoutExerciseType(WorkoutExerciseType.TIME);

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

    public static WorkoutExerciseUpdateRequest createUpdateRequest(Long id, Integer order) {
        return WorkoutExerciseUpdateRequest.builder()
                .id(id)
                .order(order)
                .note(UUID.randomUUID().toString())
                .workoutExerciseType(WorkoutExerciseType.BODYWEIGHT)
                .build();
    }

    public static WorkoutExerciseCreateRequest createCreateRequest(Long workoutId, Long exerciseId, Integer order) {
        return WorkoutExerciseCreateRequest.builder()
                .workoutId(workoutId)
                .exerciseId(exerciseId)
                .order(order)
                .note(UUID.randomUUID().toString())
                .workoutExerciseType(WorkoutExerciseType.WEIGHT)
                .build();
    }

    public static void assertGet(WorkoutExercise workoutExercise, WorkoutExerciseResponse response, List<WorkoutExerciseSet> expectedWorkoutExerciseSetList) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutExercise.getId(), response.id());
        Assertions.assertEquals(workoutExercise.getWorkout().getId(), response.workoutId());
        Assertions.assertEquals(workoutExercise.getExercise().getName(), response.exerciseName());
        Assertions.assertEquals(workoutExercise.getNote(), response.note());
        EnumHelper.assertEnumResponse(workoutExercise.getWorkoutExerciseType(), response.workoutExerciseTypeResponse());
        Assertions.assertNotNull(response.workoutExerciseSetResponseList());
        Assertions.assertEquals(expectedWorkoutExerciseSetList.size(), response.workoutExerciseSetResponseList().size());
        List<WorkoutExerciseSet> sortedExpectedWorkoutExerciseSetList = expectedWorkoutExerciseSetList.stream().sorted(Comparator.comparing(WorkoutExerciseSet::getOrder)).toList();
        for (int i = 0; i < response.workoutExerciseSetResponseList().size(); i++) {
            WorkoutExerciseSet expectedWorkoutExerciseSet = sortedExpectedWorkoutExerciseSetList.get(i);
            WorkoutExerciseSetResponse workoutExerciseSetResponse = response.workoutExerciseSetResponseList().get(i);
            WorkoutExerciseSetHelper.assertGet(expectedWorkoutExerciseSet, workoutExerciseSetResponse);
        }
    }

    public static void assertDelete(boolean exists, WorkoutExercise workoutExercise, Long deletedWorkoutExerciseId) {
        assertFalse(exists);
        assertEquals(workoutExercise.getId(), deletedWorkoutExerciseId);
    }

    public static void assertFilter(PageResponse<WorkoutExerciseResponse> response, List<WorkoutExercise> expectedList) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getResults());
        Assertions.assertEquals(expectedList.size(), response.getResults().size());
        List<WorkoutExerciseResponse> sortedResults = response.getResults().stream().sorted(Comparator.comparing(WorkoutExerciseResponse::id)).toList();
        List<WorkoutExercise> sortedExpectedList = expectedList.stream().sorted(Comparator.comparing(WorkoutExercise::getId)).toList();
        for (int i = 0; i < sortedExpectedList.size(); i++) {
            WorkoutExercise expected = sortedExpectedList.get(i);
            WorkoutExerciseResponse workoutExerciseResponse = sortedResults.get(i);
            assertGet(expected, workoutExerciseResponse, new ArrayList<>());
        }
    }

    public static void assertBatchUpdate(List<WorkoutExerciseUpdateRequest> requestList, SimpleListResponse<WorkoutExerciseResponse> listResponse) {
        Assertions.assertNotNull(listResponse);
        Assertions.assertNotNull(listResponse.getResult());
        Assertions.assertEquals(requestList.size(), listResponse.getResult().size());
        List<WorkoutExerciseUpdateRequest> sortedRequestList = requestList.stream().sorted(Comparator.comparingLong(WorkoutExerciseUpdateRequest::id)).toList();
        List<WorkoutExerciseResponse> sortedResponseList = listResponse.getResult().stream().sorted(Comparator.comparingLong(WorkoutExerciseResponse::id)).toList();
        for (int i = 0; i < sortedResponseList.size(); i++) {
            WorkoutExerciseUpdateRequest request = sortedRequestList.get(i);
            WorkoutExerciseResponse response = sortedResponseList.get(i);
            Assertions.assertEquals(request.id(), response.id());
            Assertions.assertEquals(request.order(), response.order());
            Assertions.assertEquals(request.note(), response.note());
        }
    }

    public static void assertCreate(
            WorkoutExerciseCreateRequest request,
            WorkoutExerciseResponse response,
            Integer expectedInsertedOrder,
            List<WorkoutExercise> finalWorkoutExerciseList,
            List<Integer> expectedFinalOrder
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(request.workoutId(), response.workoutId());
        Assertions.assertEquals(expectedInsertedOrder, response.order());
        Assertions.assertEquals(request.note(), response.note());
        EnumHelper.assertEnumResponse(request.workoutExerciseType(), response.workoutExerciseTypeResponse());

        Assertions.assertNotNull(finalWorkoutExerciseList);
        Assertions.assertFalse(finalWorkoutExerciseList.isEmpty());
        List<Integer> finalWorkoutExerciseListOrderList = finalWorkoutExerciseList.stream().map(WorkoutExercise::getOrder).sorted().toList();
        Assertions.assertEquals(expectedFinalOrder, finalWorkoutExerciseListOrderList);
    }

    public static void assertUpdate(
            WorkoutExerciseUpdateRequest request,
            WorkoutExerciseResponse response,
            List<Long> idsOfExpectedElementsInOrder,
            List<WorkoutExercise> finalWorkoutExerciseList
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.id(), response.id());
        Assertions.assertNotNull(response.workoutId());
        EnumHelper.assertEnumResponse(request.workoutExerciseType(), response.workoutExerciseTypeResponse());
        Assertions.assertNotNull(finalWorkoutExerciseList);
        Assertions.assertEquals(request.note(), response.note());
        Assertions.assertFalse(finalWorkoutExerciseList.isEmpty());
        Assertions.assertEquals(idsOfExpectedElementsInOrder.size(), finalWorkoutExerciseList.size());
        List<Long> actualIdsInOrder = finalWorkoutExerciseList.stream()
                .map(WorkoutExercise::getId)
                .toList();
        Assertions.assertEquals(idsOfExpectedElementsInOrder, actualIdsInOrder);
    }
}
