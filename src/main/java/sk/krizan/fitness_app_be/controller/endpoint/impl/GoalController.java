package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.mapper.GoalMapper;
import sk.krizan.fitness_app_be.service.api.GoalService;

@RestController
@RequiredArgsConstructor
public class GoalController implements sk.krizan.fitness_app_be.controller.endpoint.api.GoalController {

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
