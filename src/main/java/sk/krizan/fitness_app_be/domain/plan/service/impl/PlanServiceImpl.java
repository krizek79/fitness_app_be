package sk.krizan.fitness_app_be.domain.plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.coaching_contract.service.api.CoachingContractService;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.mapper.PlanMapper;
import sk.krizan.fitness_app_be.domain.plan.repository.PlanRepository;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanSimpleResponse;
import sk.krizan.fitness_app_be.domain.plan.service.api.PlanService;
import sk.krizan.fitness_app_be.domain.plan.specification.PlanSpecification;
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
public class PlanServiceImpl implements PlanService {

    private final WeekService weekService;
    private final UserService userService;
    private final PlanRepository planRepository;
    private final CoachingContractService coachingContractService;

    private static final List<String> supportedSortFields = List.of(
            Plan.Fields.id,
            Plan.Fields.title
    );

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PlanSimpleResponse> filterPlans(PlanFilterRequest request) {
        User currentUser = userService.getOrCreateCurrentUser();
        boolean isUserAdmin = userService.isUserAdmin(currentUser);

        Specification<Plan> specification = PlanSpecification.filter(request, currentUser, isUserAdmin);

        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );

        Page<Plan> page = planRepository.findAll(specification, pageable);

        List<PlanSimpleResponse> responseList = page.stream()
                .map(PlanMapper::entityToSimpleResponse)
                .collect(Collectors.toList());

        return PageResponse.<PlanSimpleResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Plan getPlanById(Long id) {
        return planRepository.getByIdOrThrow(id);
    }

    @Override
    @Transactional
    public Plan createUpdatePlan(Long id, PlanInputRequest request) {
        Plan plan;
        Profile trainee;
        Profile author;

        User currentUser = userService.getOrCreateCurrentUser();

        boolean isNew = id == null;
        if (!isNew) {
            plan = getPlanById(id);
            author = plan.getAuthor();
        } else {
            plan = new Plan();
            author = currentUser.getProfile();
        }

        trainee = coachingContractService.resolveTrainee(request.traineeId(), author, isNew ? author : plan.getTrainee());

        PlanMapper.inputRequestToEntity(isNew, request, plan, author, trainee);

        resolveWeeks(plan, request.weeks());

        return planRepository.save(plan);
    }

    private void resolveWeeks(Plan plan, List<WeekInputRequest> weeks) {
        Set<Long> incomingIds = weeks.stream().map(WeekInputRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        // Remove weeks that are not in the incoming request
        plan.getWeeks().removeIf(week -> week.getId() != null && !incomingIds.contains(week.getId()));

        // Add or update weeks from the incoming request
        for (WeekInputRequest weekInputRequest : weeks) {
            weekService.createUpdateWeek(plan, weekInputRequest);
        }

        //  Ensure the order of weeks is consistent with the input request
        plan.getWeeks().sort(Comparator.comparing(
                Week::getOrder,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));

        int currentOrder = 1;
        for (Week week : plan.getWeeks()) {
            week.setOrder(currentOrder++);
        }
    }

    @Override
    @Transactional
    public void deletePlan(Long id) {
        Plan plan = getPlanById(id);
        planRepository.delete(plan);
    }

}

