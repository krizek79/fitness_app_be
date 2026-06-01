package sk.krizan.fitness_app_be.domain.goal.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoalMapper {

    public static GoalResponse entityToResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .profile(ProfileMapper.entityToSimpleResponse(goal.getProfile()))
                .text(goal.getText())
                .achieved(goal.getAchieved())
                .build();
    }

    public static Goal inputRequestToEntity(Goal goal, GoalInputRequest request, Profile profile) {
        if (goal == null) {
            goal = new Goal();
            profile.addToGoals(goal);
        }

        goal.setText(request.text());
        goal.setAchieved(request.achieved());

        return goal;
    }

}
