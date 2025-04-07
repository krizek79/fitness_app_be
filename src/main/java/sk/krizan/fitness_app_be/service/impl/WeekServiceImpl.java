package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
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

    private final static String ERROR_WEEK_NOT_FOUND = "Week with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            Week.Fields.id
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
        Week week = WeekMapper.createRequestToEntity(request, cycle);
        return weekRepository.save(week);
    }

    @Override
    @Transactional
    public Week updateWeek(Long id, WeekUpdateRequest request) {
        Week week = getWeekById(id);
        User currentUser = userService.getCurrentUser();
        if (week.getCycle().getAuthor().getUser() != currentUser && !currentUser.getRoleSet().contains(Role.ADMIN)) {
            throw new ForbiddenException();
        }

        return weekRepository.save(WeekMapper.updateRequestToEntity(request, week));
    }

    @Override
    public Long deleteWeek(Long id) {
        User currentUser = userService.getCurrentUser();
        Week week = getWeekById(id);

        if (week.getCycle() != null
                && week.getCycle().getAuthor() != null
                && week.getCycle().getAuthor().getUser() != currentUser
                && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        weekRepository.delete(week);
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
