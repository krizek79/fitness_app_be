package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.model.mapper.GoalMapper;
import sk.krizan.fitness_app_be.service.api.GoalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("goals")
public class GoalController {

    private final GoalService goalService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<GoalResponse> filterGoals(@Valid @RequestBody GoalFilterRequest request) {
        return goalService.filterGoals(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public GoalResponse getGoalById(@PathVariable Long id) {
        return GoalMapper.entityToResponse(goalService.getGoalById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public GoalResponse createGoal(@Valid @RequestBody GoalCreateRequest request) {
        return GoalMapper.entityToResponse(goalService.createGoal(request));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public GoalResponse updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody GoalUpdateRequest request
    ) {
        return GoalMapper.entityToResponse(goalService.updateGoal(id, request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteGoal(@PathVariable Long id) {
        return goalService.deleteGoal(id);
    }

    @PutMapping("{id}/trigger-achieved")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public GoalResponse triggerAchieved(@PathVariable Long id) {
        return GoalMapper.entityToResponse(goalService.triggerAchieved(id));
    }
}
