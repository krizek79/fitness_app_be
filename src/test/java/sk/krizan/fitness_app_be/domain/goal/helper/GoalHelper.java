package sk.krizan.fitness_app_be.domain.goal.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GoalHelper {

    public static Goal createGoal(Profile profile, boolean achieved) {
        Goal goal = new Goal();
        goal.setText(UUID.randomUUID().toString());
        goal.setAchieved(achieved);

        profile.addToGoals(goal);

        return goal;
    }

    public static GoalInputRequest createInputRequest(
            Boolean achieved
    ) {
        return GoalInputRequest.builder()
                .achieved(achieved)
                .text(UUID.randomUUID().toString())
                .build();
    }

    public static GoalFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long profileId,
            boolean achieved
    ) {
        return GoalFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .profileId(profileId)
                .achieved(achieved)
                .build();
    }

    public static void assertGoalResponse(Goal goal, GoalResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(goal.getText(), response.text());
        Assertions.assertEquals(goal.getAchieved(), response.achieved());
        ProfileHelper.assertProfileSimpleResponse(goal.getProfile(), response.profile());
    }

    public static void assertInputToEntity(Goal goal, GoalInputRequest request) {
        Assertions.assertNotNull(goal);
        Assertions.assertNotNull(goal.getId());
        Assertions.assertNotNull(goal.getProfile());
        Assertions.assertEquals(request.text(), goal.getText());
        Assertions.assertEquals(request.achieved(), goal.getAchieved());
    }

}
