package sk.krizan.fitness_app_be.domain.week.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.SimpleListResponse;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;
import sk.krizan.fitness_app_be.domain.week.mapper.WeekMapper;
import sk.krizan.fitness_app_be.domain.week.service.api.WeekService;

@RestController
@RequiredArgsConstructor
public class WeekController implements sk.krizan.fitness_app_be.domain.week.rest.api.WeekController {

    private final WeekService weekService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<WeekResponse> filterWeeks(WeekFilterRequest request) {
        return weekService.filterWeeks(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WeekResponse getWeekById(Long id) {
        return WeekMapper.entityToResponse(weekService.getWeekById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WeekResponse createWeek(WeekCreateRequest request) {
        return WeekMapper.entityToResponse(weekService.createWeek(request));
    }

    @Deprecated
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WeekResponse updateWeek(WeekUpdateRequest request) {
        return WeekMapper.entityToResponse(weekService.updateWeek(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public SimpleListResponse<WeekResponse> batchUpdateWeeks(BatchUpdateRequest<WeekUpdateRequest> request) {
        return WeekMapper.entityListToSimpleListResponse(weekService.batchUpdateWeeks(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public Long deleteWeek(Long id) {
        return weekService.deleteWeek(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WeekResponse triggerCompleted(Long id) {
        return WeekMapper.entityToResponse(weekService.triggerCompleted(id));
    }
}
