package sk.krizan.fitness_app_be.domain.week.service.impl;

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
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.mapper.WeekMapper;
import sk.krizan.fitness_app_be.domain.week.repository.WeekRepository;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;
import sk.krizan.fitness_app_be.domain.week.service.api.WeekService;
import sk.krizan.fitness_app_be.domain.week.specification.WeekSpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekServiceImpl implements WeekService {

    private final WeekRepository weekRepository;

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
        return weekRepository.getByIdOrThrow(id);
    }

    @Override
    public void createUpdateWeek(Plan plan, WeekInputRequest weekInputRequest) {
        Week week;
        if (weekInputRequest.id() != null) {
            //  Update existing week
            week = plan.getWeeks().stream()
                    .filter(we -> we.getId().equals(weekInputRequest.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Week with id %d not found in plan with id %d.".formatted(weekInputRequest.id(), plan.getId())));
            WeekMapper.inputRequestToEntity(week, weekInputRequest, plan);
        } else {
            //  Create new week
            WeekMapper.inputRequestToEntity(null, weekInputRequest, plan);
        }
    }
}
