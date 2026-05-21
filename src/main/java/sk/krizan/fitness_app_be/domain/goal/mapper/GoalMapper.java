package sk.krizan.fitness_app_be.domain.goal.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoalMapper {

    public static GoalResponse entityToResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .cycleId(goal.getCycle() != null ? goal.getCycle().getId() : null)
                .text(goal.getText())
                .achieved(goal.getAchieved())
                .build();
    }

    public static void inputRequestToEntity(Goal goal, GoalInputRequest request, Cycle cycle) {
        if (goal == null) {
            goal = new Goal();
            cycle.addToGoals(goal);
        }

        goal.setText(request.text());
        goal.setAchieved(request.achieved());
    }

}
