package sk.krizan.fitness_app_be.domain.goal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.mapper.GoalMapper;
import sk.krizan.fitness_app_be.domain.goal.repository.GoalRepository;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.domain.goal.service.api.GoalService;
import sk.krizan.fitness_app_be.domain.goal.specification.GoalSpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    private static final List<String> supportedSortFields = List.of(
            Goal.Fields.id
    );

    /**
     * Retrieves a paginated list of goals based on the specified filter criteria.
     *
     * @param request the request containing the filter criteria for retrieving goals. The request may include parameters such as page number, page size, sorting options, and various filters based on workout attributes.
     * @return a paginated response containing a list of goals that match the specified filter criteria. The response includes metadata about the pagination, such as total pages, total elements, and the current page number.
     */
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

    /**
     * Creates a new goal or updates an existing goal for the specified plan based on the provided request data.
     *
     * @param plan the plan for which the goal is being created or updated
     * @param request the request containing the data for creating or updating the goal
     */
    @Override
    public void createUpdateGoal(Plan plan, GoalInputRequest request) {
        Goal goal;
        if (request.id() != null) {
            //  Update existing goal
            goal = plan.getGoals().stream()
                    .filter(g -> g.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Goal with id %d not found in plan with id %d.".formatted(request.id(), plan.getId())));
            GoalMapper.inputRequestToEntity(goal, request, plan);
        } else {
            //  Create new goal
            GoalMapper.inputRequestToEntity(null, request, plan);
        }
    }

}
