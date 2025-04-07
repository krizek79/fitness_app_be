package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Goal;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.GoalMapper;
import sk.krizan.fitness_app_be.repository.GoalRepository;
import sk.krizan.fitness_app_be.service.api.CycleService;
import sk.krizan.fitness_app_be.service.api.GoalService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.specification.GoalSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final UserService userService;
    private final CycleService cycleService;
    private final GoalRepository goalRepository;

    private final static String ERROR_GOAL_NOT_FOUND = "Goal with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            Goal.Fields.id
    );

    @Override
    @Transactional
    public PageResponse<GoalResponse> filterGoals(GoalFilterRequest request) {
        Specification<Goal> specification = GoalSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Goal> page = goalRepository.findAll(specification, pageable);
        List<GoalResponse> responseList = page.stream()
                .map(GoalMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<GoalResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Goal getGoalById(Long id) {
        return goalRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_GOAL_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public Goal createGoal(GoalCreateRequest request) {
        Cycle cycle = cycleService.getCycleById(request.cycleId());
        Goal goal = GoalMapper.createRequestToEntity(request, cycle);
        return goalRepository.save(goal);
    }

    @Override
    @Transactional
    public Goal updateGoal(Long id, GoalUpdateRequest request) {
        User currentUser = userService.getCurrentUser();
        Goal goal = getGoalById(id);

        if (goal.getCycle() != null
                && goal.getCycle().getAuthor() != null
                && goal.getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        return goalRepository.save(GoalMapper.updateRequestToEntity(request, goal));
    }

    @Override
    public Long deleteGoal(Long id) {
        User currentUser = userService.getCurrentUser();
        Goal goal = getGoalById(id);

        if (goal.getCycle() != null
                && goal.getCycle().getAuthor() != null
                && goal.getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        goalRepository.delete(goal);
        return goal.getId();
    }

    @Override
    @Transactional
    public Goal triggerAchieved(Long id) {
        User currentUser = userService.getCurrentUser();
        Goal goal = getGoalById(id);

        if (goal.getCycle() != null
                && goal.getCycle().getAuthor() != null
                && goal.getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        goal.setAchieved(!goal.getAchieved());
        return goalRepository.save(goal);
    }
}
