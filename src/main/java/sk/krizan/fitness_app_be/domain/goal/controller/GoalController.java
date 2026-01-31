package sk.krizan.fitness_app_be.domain.goal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.goal.mapper.GoalMapper;
import sk.krizan.fitness_app_be.domain.goal.service.api.GoalService;

@RestController
@RequiredArgsConstructor
public class GoalController implements sk.krizan.fitness_app_be.domain.goal.rest.api.GoalController {

    private final GoalService goalService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<GoalResponse> filterGoals(GoalFilterRequest request) {
        return goalService.filterGoals(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public GoalResponse getGoalById(Long id) {
        return GoalMapper.entityToResponse(goalService.getGoalById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public GoalResponse createGoal(GoalCreateRequest request) {
        return GoalMapper.entityToResponse(goalService.createGoal(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public GoalResponse updateGoal(Long id, GoalUpdateRequest request) {
        return GoalMapper.entityToResponse(goalService.updateGoal(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public Long deleteGoal(Long id) {
        return goalService.deleteGoal(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public GoalResponse triggerAchieved(Long id) {
        return GoalMapper.entityToResponse(goalService.triggerAchieved(id));
    }
}
