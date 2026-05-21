package sk.krizan.fitness_app_be.domain.cycle.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.coach_client.service.api.CoachClientService;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.cycle.mapper.CycleMapper;
import sk.krizan.fitness_app_be.domain.cycle.repository.CycleRepository;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleInputRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
import sk.krizan.fitness_app_be.domain.cycle.service.api.CycleService;
import sk.krizan.fitness_app_be.domain.cycle.specification.CycleSpecification;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.service.api.GoalService;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.service.api.WeekService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final GoalService goalService;
    private final WeekService weekService;
    private final UserService userService;
    private final CycleRepository cycleRepository;
    private final CoachClientService coachClientService;

    private static final List<String> supportedSortFields = List.of(
            Cycle.Fields.id,
            Cycle.Fields.title
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CycleResponse> filterCycles(CycleFilterRequest request) {
        User currentUser = userService.getCurrentUser();

        Specification<Cycle> specification;

        specification = CycleSpecification.filter(request, currentUser);

        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );

        Page<Cycle> page = cycleRepository.findAll(specification, pageable);

        List<CycleResponse> responseList = page.stream()
                .map(CycleMapper::entityToResponse)
                .collect(Collectors.toList());

        return PageResponse.<CycleResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Cycle getCycleById(Long id) {
        return cycleRepository.getByIdOrThrow(id);
    }

    @Override
    @Transactional
    public Cycle createUpdateCycle(Long id, CycleInputRequest request) {
        Cycle cycle;
        Profile trainee;
        Profile author;

        User currentUser = userService.getCurrentUser();

        boolean isNew = id == null;
        if (!isNew) {
            cycle = getCycleById(id);
            author = cycle.getAuthor();
        } else {
            cycle = new Cycle();
            author = currentUser.getProfile();
        }

        trainee = coachClientService.resolveTrainee(request.traineeId(), author, isNew ? author : cycle.getTrainee());

        CycleMapper.inputRequestToEntity(isNew, request, cycle, author, trainee);

        resolveWeeks(cycle, request.weeks());
        resolveGoals(cycle, request.goals());

        return cycleRepository.save(cycle);
    }

    private void resolveWeeks(Cycle cycle, List<WeekInputRequest> weeks) {
        Set<Long> incomingIds = weeks.stream().map(WeekInputRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        // Remove weeks that are not in the incoming request
        cycle.getWeeks().removeIf(week -> week.getId() != null && !incomingIds.contains(week.getId()));

        // Add or update weeks from the incoming request
        for (WeekInputRequest weekInputRequest : weeks) {
            weekService.createUpdateWeek(cycle, weekInputRequest);
        }

        //  Ensure the order of weeks is consistent with the input request
        cycle.getWeeks().sort(Comparator.comparing(
                Week::getOrder,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));

        int currentOrder = 1;
        for (Week week : cycle.getWeeks()) {
            week.setOrder(currentOrder++);
        }
    }

    private void resolveGoals(Cycle cycle, List<GoalInputRequest> goals) {
        Set<Long> incomingIds = goals.stream().map(GoalInputRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        //  Remove goals that are not in the incoming request
        cycle.getGoals().removeIf(goal -> goal.getId() != null && !incomingIds.contains(goal.getId()));

        //  Add or update goals from the incoming request
        for (GoalInputRequest goalInputRequest : goals) {
            goalService.createUpdateGoal(cycle, goalInputRequest);
        }
    }

    @Override
    @Transactional
    public void deleteCycle(Long id) {
        Cycle cycle = getCycleById(id);
        cycleRepository.delete(cycle);
    }

}

