package sk.krizan.fitness_app_be.domain.goal.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoalMapper {

    public static GoalResponse entityToResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .planId(goal.getPlan() != null ? goal.getPlan().getId() : null)
                .text(goal.getText())
                .achieved(goal.getAchieved())
                .build();
    }

    public static void inputRequestToEntity(Goal goal, GoalInputRequest request, Plan plan) {
        if (goal == null) {
            goal = new Goal();
            plan.addToGoals(goal);
        }

        goal.setText(request.text());
        goal.setAchieved(request.achieved());
    }

}
