package sk.krizan.fitness_app_be.domain.goal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.goal.mapper.GoalMapper;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.domain.goal.service.api.GoalService;

@RestController
@RequiredArgsConstructor
public class GoalController implements sk.krizan.fitness_app_be.domain.goal.rest.api.GoalController {

    private final GoalService goalService;

    @Override
    public PageResponse<GoalResponse> filterGoals(GoalFilterRequest request) {
        return goalService.filterGoals(request);
    }

    @Override
    public GoalResponse createGoal(GoalInputRequest request) {
        return GoalMapper.entityToResponse(goalService.createUpdateGoal(null, request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'GOAL', 'UPDATE')")
    public GoalResponse updateGoal(Long id, GoalInputRequest request) {
        return GoalMapper.entityToResponse(goalService.createUpdateGoal(id, request));
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'GOAL', 'DELETE')")
    public void deleteGoal(Long id) {
        goalService.deleteGoal(id);
    }

}
