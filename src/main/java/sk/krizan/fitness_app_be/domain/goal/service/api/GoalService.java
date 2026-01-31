package sk.krizan.fitness_app_be.domain.goal.service.api;

import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;

public interface GoalService {

    PageResponse<GoalResponse> filterGoals(GoalFilterRequest request);

    Goal getGoalById(Long id);

    Goal createGoal(GoalCreateRequest request);

    Goal updateGoal(Long id, GoalUpdateRequest request);

    Long deleteGoal(Long id);

    Goal triggerAchieved(Long id);
}
