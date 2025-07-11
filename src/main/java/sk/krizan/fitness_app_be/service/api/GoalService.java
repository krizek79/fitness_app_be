package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Goal;

public interface GoalService {

    PageResponse<GoalResponse> filterGoals(GoalFilterRequest request);

    Goal getGoalById(Long id);

    Goal createGoal(GoalCreateRequest request);

    Goal updateGoal(Long id, GoalUpdateRequest request);

    Long deleteGoal(Long id);

    Goal triggerAchieved(Long id);
}
