package sk.krizan.fitness_app_be.domain.goal.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GoalHelper {

    public static Goal createGoal() {
        Goal goal = new Goal();
        goal.setText(UUID.randomUUID().toString());

        return goal;
    }

    public static GoalInputRequest createInputRequest(
            Long id,
            Boolean achieved
    ) {
        return GoalInputRequest.builder()
                .id(id)
                .achieved(achieved)
                .text(UUID.randomUUID().toString())
                .build();
    }

    public static GoalFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long planId
    ) {
        return GoalFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .planId(planId)
                .build();
    }

    public static void assertGoalResponse(Goal goal, GoalResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(goal.getPlan().getId(), response.planId());
        Assertions.assertEquals(goal.getText(), response.text());
        Assertions.assertEquals(goal.getAchieved(), response.achieved());
    }

    public static void assertInputToEntity(Goal goal, GoalInputRequest request) {
        Assertions.assertNotNull(goal);

        if (request.id() != null) {
            Assertions.assertEquals(request.id(), goal.getId());
        } else {
            Assertions.assertNotNull(goal.getId());
        }

        Assertions.assertEquals(request.text(), goal.getText());
        Assertions.assertEquals(request.achieved(), goal.getAchieved());
    }

}
