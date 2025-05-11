package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Week;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WeekHelper {

    public static Week createMockWeek(Cycle cycle, Integer order) {
        Week week = new Week();
        week.setCycle(cycle);
        week.setOrder(order);

        cycle.addToWeekList(List.of(week));

        return week;
    }

    public static WeekCreateRequest createCreateRequest(Long cycleId, Integer order) {
        return WeekCreateRequest.builder()
                .cycleId(cycleId)
                .order(order)
                .build();
    }

    public static WeekUpdateRequest createUpdateRequest(Long id, Integer order) {
        return WeekUpdateRequest.builder()
                .id(id)
                .order(order)
                .build();
    }

    public static void assertGet(Week week, WeekResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(week.getId(), response.id());
        Assertions.assertEquals(week.getCycle().getId(), response.cycleId());
        Assertions.assertEquals(week.getOrder(), response.order());
        Assertions.assertFalse(response.completed());
    }

    public static void assertDelete(boolean exists, Week savedMockWeek, Long deletedWeekId, Week weekToUpdate) {
        assertFalse(exists);
        assertEquals(savedMockWeek.getId(), deletedWeekId);
        Assertions.assertEquals(1, weekToUpdate.getOrder());
    }

    public static void assertTriggerCompleted(Boolean originalState, WeekResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(!originalState, response.completed());
    }

    public static WeekFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long cycleId
    ) {
        return WeekFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .cycleId(cycleId)
                .build();
    }

    public static void assertFilter(
            List<Week> expectedList,
            WeekFilterRequest request,
            PageResponse<WeekResponse> response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.pageNumber());
        Assertions.assertNotNull(response.pageSize());
        Assertions.assertNotNull(response.totalElements());
        Assertions.assertNotNull(response.totalPages());
        Assertions.assertNotNull(response.results());
        Assertions.assertFalse(response.results().isEmpty());
        Assertions.assertEquals(request.page(), response.pageNumber());
        Assertions.assertEquals(expectedList.size(), response.results().size());

        List<WeekResponse> results = response.results();
        results.sort(Comparator.comparingLong(WeekResponse::id));
        expectedList.sort(Comparator.comparingLong(Week::getId));
        for (int i = 0; i < results.size(); i++) {
            WeekResponse weekResponse = results.get(i);
            Week week = expectedList.get(i);
            assertGet(week, weekResponse);
        }
    }

    public static void assertCreateWeek(
            WeekCreateRequest request,
            WeekResponse response,
            Integer expectedInsertedOrder,
            List<Week> finalWeekList,
            List<Integer> expectedFinalOrder
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(request.cycleId(), response.cycleId());
        Assertions.assertFalse(response.completed());
        Assertions.assertEquals(expectedInsertedOrder, response.order());

        Assertions.assertNotNull(finalWeekList);
        Assertions.assertFalse(finalWeekList.isEmpty());
        List<Integer> finalWeekListOrderList = finalWeekList.stream().map(Week::getOrder).sorted().toList();
        Assertions.assertEquals(expectedFinalOrder, finalWeekListOrderList);
    }

    public static void assertUpdateWeek(
            WeekUpdateRequest request,
            WeekResponse response,
            List<Long> idsOfExpectedElementsInOrder,
            List<Week> finalWeekList
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.id(), response.id());
        Assertions.assertNotNull(response.cycleId());
        Assertions.assertFalse(response.completed());
        Assertions.assertNotNull(finalWeekList);
        Assertions.assertFalse(finalWeekList.isEmpty());
        Assertions.assertEquals(idsOfExpectedElementsInOrder.size(), finalWeekList.size());
        List<Long> actualIdsInOrder = finalWeekList.stream()
                .map(Week::getId)
                .toList();
        Assertions.assertEquals(idsOfExpectedElementsInOrder, actualIdsInOrder);
    }

    public static void assertBatchUpdate(List<WeekUpdateRequest> requestList, SimpleListResponse<WeekResponse> listResponse) {
        Assertions.assertNotNull(listResponse);
        Assertions.assertNotNull(listResponse.result());
        Assertions.assertEquals(requestList.size(), listResponse.result().size());
        List<WeekUpdateRequest> sortedRequestList = requestList.stream().sorted(Comparator.comparingLong(WeekUpdateRequest::id)).toList();
        List<WeekResponse> sortedResponseList = listResponse.result().stream().sorted(Comparator.comparingLong(WeekResponse::id)).toList();
        for (int i = 0; i < sortedResponseList.size(); i++) {
            WeekUpdateRequest request = sortedRequestList.get(i);
            WeekResponse response = sortedResponseList.get(i);
            Assertions.assertEquals(request.id(), response.id());
            Assertions.assertEquals(request.order(), response.order());
        }
    }
}
