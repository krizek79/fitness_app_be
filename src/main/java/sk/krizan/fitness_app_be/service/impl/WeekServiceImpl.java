package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.event.EntityLifeCycleEventEnum;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.WeekMapper;
import sk.krizan.fitness_app_be.repository.WeekRepository;
import sk.krizan.fitness_app_be.service.api.CycleService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WeekService;
import sk.krizan.fitness_app_be.specification.WeekSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekServiceImpl implements WeekService {

    private final UserService userService;
    private final CycleService cycleService;
    private final WeekRepository weekRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final static String ERROR_WEEK_NOT_FOUND = "Week with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            Week.Fields.id,
            Week.Fields.order
    );

    @Override
    @Transactional
    public PageResponse<WeekResponse> filterWeeks(WeekFilterRequest request) {
        Specification<Week> specification = WeekSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Week> page = weekRepository.findAll(specification, pageable);
        List<WeekResponse> responseList = page.stream()
                .map(WeekMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<WeekResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Week getWeekById(Long id) {
        return weekRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_WEEK_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public Week createWeek(WeekCreateRequest request) {
        Cycle cycle = cycleService.getCycleById(request.cycleId());
        Week week = weekRepository.save(WeekMapper.createRequestToEntity(request, cycle));
        applicationEventPublisher.publishEvent(new EntityReorderEvent(week, EntityLifeCycleEventEnum.CREATE));
        return week;
    }

    @Override
    @Transactional
    public Week updateWeek(WeekUpdateRequest request) {
        Week week = getWeekById(request.id());
        User currentUser = userService.getCurrentUser();

        if (week.getCycle().getAuthor().getUser() != currentUser && !currentUser.getRoleSet().contains(Role.ADMIN)) {
            throw new ForbiddenException();
        }

        int originalOrder = week.getOrder();
        week = weekRepository.save(WeekMapper.updateRequestToEntity(request, week));
        applicationEventPublisher.publishEvent(new EntityReorderEvent(week, EntityLifeCycleEventEnum.UPDATE, originalOrder));
        return week;
    }

    @Override
    @Transactional
    public List<Week> batchUpdateWeeks(BatchUpdateRequest<WeekUpdateRequest> request) {
        List<Week> updatedWeeks = request.updateRequestList().stream()
                .map(this::updateWeek)
                .toList();

        return weekRepository.saveAll(updatedWeeks);
    }

    @Override
    public Long deleteWeek(Long id) {
        User currentUser = userService.getCurrentUser();
        Week week = getWeekById(id);

        if (week.getCycle() == null) {
            throw new RuntimeException("Cycle is null.");
        }

        if (week.getCycle().getAuthor() != null
                && week.getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        week.getCycle().removeFromWeekList(week);
        weekRepository.delete(week);
        applicationEventPublisher.publishEvent(new EntityReorderEvent(week, EntityLifeCycleEventEnum.DELETE));

        return week.getId();
    }

    @Override
    @Transactional
    public Week triggerCompleted(Long id) {
        User currentUser = userService.getCurrentUser();
        Week week = getWeekById(id);

        if (week.getCycle() != null
                && week.getCycle().getAuthor() != null
                && week.getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }
        week.setCompleted(!week.getCompleted());
        return weekRepository.save(week);
    }
}
