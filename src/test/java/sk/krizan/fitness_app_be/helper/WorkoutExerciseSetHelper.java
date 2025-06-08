package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseSetHelper {

    public static WorkoutExerciseSet createMockWorkoutExerciseSet(WorkoutExercise workoutExercise, Integer order) {
        WorkoutExerciseSet workoutExerciseSet = new WorkoutExerciseSet();
        workoutExerciseSet.setWorkoutExercise(workoutExercise);
        workoutExerciseSet.setOrder(order);
        workoutExerciseSet.setWorkoutExerciseSetType(WorkoutExerciseSetType.STRAIGHT_SET);
        workoutExerciseSet.setGoalRepetitions(RandomHelper.getRandomInt(1, 15));
        workoutExerciseSet.setActualRepetitions(RandomHelper.getRandomInt(1, 15));
        workoutExerciseSet.setGoalWeight(RandomHelper.getRandomDouble(0.125, 150));
        workoutExerciseSet.setActualWeight(RandomHelper.getRandomDouble(0.125, 150));
        workoutExerciseSet.setGoalTime(Duration.ofMinutes(45));
        workoutExerciseSet.setActualTime(Duration.ofMinutes(41));
        workoutExerciseSet.setRestDuration(Duration.ofMinutes(5));
        workoutExerciseSet.setNote(UUID.randomUUID().toString());

        workoutExercise.addToWorkoutExerciseSetList(List.of(workoutExerciseSet));

        return workoutExerciseSet;
    }

    public static WorkoutExerciseSetFilterRequest createFilterRequest(Integer page, Integer size, String sortBy, String sortDirection, Long workoutExerciseId) {
        return WorkoutExerciseSetFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .workoutExerciseId(workoutExerciseId)
                .build();
    }

    public static WorkoutExerciseSetUpdateRequest createUpdateRequest(
            Long id,
            Integer order,
            String workoutExerciseSetTypeKey
    ) {
        return WorkoutExerciseSetUpdateRequest.builder()
                .id(id)
                .order(order)
                .workoutExerciseSetTypeKey(workoutExerciseSetTypeKey)
                .actualRepetitions(RandomHelper.getRandomInt(1, 15))
                .goalRepetitions(RandomHelper.getRandomInt(1, 15))
                .goalWeight(RandomHelper.getRandomDouble(2.5, 200))
                .actualWeight(RandomHelper.getRandomDouble(2.5, 200))
                .goalTime(RandomHelper.getRandomDuration(Duration.ofMinutes(1), Duration.ofMinutes(3)).toString())
                .actualTime(RandomHelper.getRandomDuration(Duration.ofMinutes(1), Duration.ofMinutes(3)).toString())
                .restDuration(RandomHelper.getRandomDuration(Duration.ofMinutes(3), Duration.ofMinutes(5)).toString())
                .note(UUID.randomUUID().toString())
                .build();
    }

    public static WorkoutExerciseSetCreateRequest createCreateRequest(Long workoutExerciseId, Integer order) {
        return WorkoutExerciseSetCreateRequest.builder()
                .order(order)
                .workoutExerciseId(workoutExerciseId)
                .workoutExerciseSetTypeKey(WorkoutExerciseSetType.STRAIGHT_SET.getKey())
                .note(UUID.randomUUID().toString())
                .goalRepetitions(RandomHelper.getRandomInt(1, 15))
                .goalWeight(RandomHelper.getRandomDouble(2.5, 200))
                .goalTime(RandomHelper.getRandomDuration(Duration.ofMinutes(1), Duration.ofMinutes(3)).toString())
                .restDuration(RandomHelper.getRandomDuration(Duration.ofMinutes(3), Duration.ofMinutes(5)).toString())
                .build();
    }

    public static void assertDelete(boolean exists, WorkoutExerciseSet workoutExerciseSet, Long deletedWorkoutExerciseSetId) {
        assertFalse(exists);
        assertEquals(workoutExerciseSet.getId(), deletedWorkoutExerciseSetId);
    }

    public static void assertGet(WorkoutExerciseSet workoutExerciseSet, WorkoutExerciseSetResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(workoutExerciseSet.getId(), response.id());
        Assertions.assertEquals(workoutExerciseSet.getWorkoutExercise().getId(), response.workoutExerciseId());
        EnumHelper.assertEnumResponse(workoutExerciseSet.getWorkoutExerciseSetType().getKey(), response.workoutExerciseSetTypeResponse());
        Assertions.assertEquals(workoutExerciseSet.getCompleted(), response.completed());
        Assertions.assertEquals(workoutExerciseSet.getOrder(), response.order());
        Assertions.assertEquals(workoutExerciseSet.getGoalWeight(), response.goalWeight());
        Assertions.assertEquals(workoutExerciseSet.getActualWeight(), response.actualWeight());
        Assertions.assertEquals(workoutExerciseSet.getGoalRepetitions(), response.goalRepetitions());
        Assertions.assertEquals(workoutExerciseSet.getActualRepetitions(), response.actualRepetitions());
        Assertions.assertEquals(workoutExerciseSet.getGoalTime().toString(), response.goalTime());
        Assertions.assertEquals(workoutExerciseSet.getActualTime().toString(), response.actualTime());
        Assertions.assertEquals(workoutExerciseSet.getRestDuration().toString(), response.restDuration());
        Assertions.assertEquals(workoutExerciseSet.getNote(), response.note());
    }

    public static void assertTriggerCompleted(Boolean originalState, WorkoutExerciseSetResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(!originalState, response.completed());
    }

    public static void assertBatchUpdate(List<WorkoutExerciseSetUpdateRequest> requestList, SimpleListResponse<WorkoutExerciseSetResponse> listResponse) {
        Assertions.assertNotNull(listResponse);
        Assertions.assertNotNull(listResponse.result());
        Assertions.assertEquals(requestList.size(), listResponse.result().size());
        List<WorkoutExerciseSetUpdateRequest> sortedRequestList = requestList.stream().sorted(Comparator.comparingLong(WorkoutExerciseSetUpdateRequest::id)).toList();
        List<WorkoutExerciseSetResponse> sortedResponseList = listResponse.result().stream().sorted(Comparator.comparingLong(WorkoutExerciseSetResponse::id)).toList();
        for (int i = 0; i < sortedResponseList.size(); i++) {
            WorkoutExerciseSetUpdateRequest request = sortedRequestList.get(i);
            WorkoutExerciseSetResponse response = sortedResponseList.get(i);
            Assertions.assertEquals(request.id(), response.id());
            Assertions.assertEquals(request.order(), response.order());
        }
    }

    public static void assertCreate(
            WorkoutExerciseSetCreateRequest request,
            WorkoutExerciseSetResponse response,
            Integer expectedInsertedOrder,
            List<WorkoutExerciseSet> finalWorkoutExerciseSetList,
            List<Integer> expectedFinalOrder
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(request.workoutExerciseId(), response.workoutExerciseId());
        Assertions.assertEquals(request.goalTime(), response.goalTime());
        Assertions.assertEquals(request.goalWeight(), response.goalWeight());
        Assertions.assertEquals(request.goalRepetitions(), response.goalRepetitions());
        Assertions.assertNull(response.actualTime());
        Assertions.assertNull(response.actualWeight());
        Assertions.assertNull(response.actualRepetitions());
        Assertions.assertFalse(response.completed());
        Assertions.assertEquals(request.restDuration(), response.restDuration());
        Assertions.assertEquals(request.note(), response.note());
        EnumHelper.assertEnumResponse(request.workoutExerciseSetTypeKey(), response.workoutExerciseSetTypeResponse());
        Assertions.assertEquals(expectedInsertedOrder, response.order());

        Assertions.assertNotNull(finalWorkoutExerciseSetList);
        Assertions.assertFalse(finalWorkoutExerciseSetList.isEmpty());
        List<Integer> finalWorkoutExerciseListOrderList = finalWorkoutExerciseSetList.stream().map(WorkoutExerciseSet::getOrder).sorted().toList();
        Assertions.assertEquals(expectedFinalOrder, finalWorkoutExerciseListOrderList);
    }

    public static void assertUpdate(
            WorkoutExerciseSetUpdateRequest request,
            WorkoutExerciseSetResponse response,
            List<Long> idsOfExpectedElementsInOrder,
            List<WorkoutExerciseSet> finalWorkoutExerciseSetList
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.id(), response.id());
        Assertions.assertNotNull(response.workoutExerciseId());
        Assertions.assertEquals(request.goalTime(), response.goalTime());
        Assertions.assertEquals(request.goalWeight(), response.goalWeight());
        Assertions.assertEquals(request.goalRepetitions(), response.goalRepetitions());
        Assertions.assertEquals(request.actualTime(), response.actualTime());
        Assertions.assertEquals(request.actualWeight(), response.actualWeight());
        Assertions.assertEquals(request.actualRepetitions(), response.actualRepetitions());
        Assertions.assertFalse(response.completed());
        Assertions.assertEquals(request.restDuration(), response.restDuration());
        Assertions.assertEquals(request.note(), response.note());
        EnumHelper.assertEnumResponse(request.workoutExerciseSetTypeKey(), response.workoutExerciseSetTypeResponse());
        Assertions.assertNotNull(finalWorkoutExerciseSetList);
        Assertions.assertFalse(finalWorkoutExerciseSetList.isEmpty());
        Assertions.assertEquals(idsOfExpectedElementsInOrder.size(), finalWorkoutExerciseSetList.size());
        List<Long> actualIdsInOrder = finalWorkoutExerciseSetList.stream()
                .map(WorkoutExerciseSet::getId)
                .toList();
        Assertions.assertEquals(idsOfExpectedElementsInOrder, actualIdsInOrder);
    }

    public static void assertFilter(PageResponse<WorkoutExerciseSetResponse> response, List<WorkoutExerciseSet> expectedList) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.results());
        Assertions.assertEquals(expectedList.size(), response.results().size());
        List<WorkoutExerciseSetResponse> sortedResults = response.results().stream().sorted(Comparator.comparing(WorkoutExerciseSetResponse::id)).toList();
        List<WorkoutExerciseSet> sortedExpectedList = expectedList.stream().sorted(Comparator.comparing(WorkoutExerciseSet::getId)).toList();
        for (int i = 0; i < sortedExpectedList.size(); i++) {
            WorkoutExerciseSet expected = sortedExpectedList.get(i);
            WorkoutExerciseSetResponse workoutExerciseSetResponse = sortedResults.get(i);
            assertGet(expected, workoutExerciseSetResponse);
        }
    }
}
