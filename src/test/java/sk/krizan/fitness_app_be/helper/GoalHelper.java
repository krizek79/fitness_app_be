package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Goal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GoalHelper {

    public static Goal createMockGoal(Cycle cycle) {
        Goal goal = new Goal();
        goal.setCycle(cycle);
        goal.setText(UUID.randomUUID().toString());
        return goal;
    }

    public static List<Goal> createMockGoalList(List<Cycle> mockCycleList) {
        List<Goal> goalList = new ArrayList<>();
        mockCycleList.forEach(cycle -> {
            for (int i = 0; i < 2; i++) {
                goalList.add(createMockGoal(cycle));
            }
        });
        return goalList;
    }

    public static GoalCreateRequest createCreateRequest(Long cycleId) {
        return GoalCreateRequest.builder()
                .cycleId(cycleId)
                .text(UUID.randomUUID().toString())
                .build();
    }

    public static GoalUpdateRequest createUpdateRequest() {
        return GoalUpdateRequest.builder()
                .text(DefaultValues.DEFAULT_UPDATE_VALUE)
                .build();
    }

    public static GoalFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long cycleId
    ) {
        return GoalFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .cycleId(cycleId)
                .build();
    }

    public static void assertGoalResponse_get(Goal goal, GoalResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(goal.getCycle().getId(), response.cycleId());
        Assertions.assertEquals(goal.getText(), response.text());
        Assertions.assertEquals(goal.getAchieved(), response.achieved());
    }

    public static void assertGoalResponse_create(GoalCreateRequest request, GoalResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.cycleId(), response.cycleId());
        Assertions.assertEquals(request.text(), response.text());
        Assertions.assertFalse(response.achieved());
    }

    public static void assertGoalResponse_update(GoalUpdateRequest request, GoalResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.cycleId());
        Assertions.assertEquals(request.text(), response.text());
        Assertions.assertFalse(response.achieved());
    }

    public static void assertDelete(boolean exists, Goal goal, Long deletedGoalId) {
        assertFalse(exists);
        assertEquals(goal.getId(), deletedGoalId);
    }

    public static void assertTriggerAchieved(Boolean originalState, GoalResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(!originalState, response.achieved());
    }

    public static void assertFilter(
            List<Goal> expectedList,
            GoalFilterRequest request,
            PageResponse<GoalResponse> response
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

        List<GoalResponse> results = response.results();
        results.sort(Comparator.comparingLong(GoalResponse::id));
        expectedList.sort(Comparator.comparingLong(Goal::getId));

        for (int i = 0; i < results.size(); i++) {
            GoalResponse goalResponse = results.get(i);
            Goal goal = expectedList.get(i);
            assertGoalResponse_get(goal, goalResponse);
        }
    }
}
