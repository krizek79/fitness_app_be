package sk.krizan.fitness_app_be.domain.goal.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;

import java.util.List;

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

    public static Goal createRequestToEntity(GoalCreateRequest request, Cycle cycle) {
        Goal goal = new Goal();
        goal.setCycle(cycle);
        goal.setText(request.text());
        cycle.addToGoalList(List.of(goal));
        return goal;
    }

    public static Goal updateRequestToEntity(GoalUpdateRequest request, Goal goal) {
        goal.setText(request.text());
        return goal;
    }
}
