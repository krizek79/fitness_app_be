package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.model.mapper.WeekMapper;
import sk.krizan.fitness_app_be.service.api.WeekService;

@RestController
@RequiredArgsConstructor
public class WeekController implements sk.krizan.fitness_app_be.controller.endpoint.api.WeekController {

    private final WeekService weekService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public PageResponse<WeekResponse> filterWeeks(WeekFilterRequest request) {
        return weekService.filterWeeks(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekResponse getWeekById(Long id) {
        return WeekMapper.entityToResponse(weekService.getWeekById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekResponse createWeek(WeekCreateRequest request) {
        return WeekMapper.entityToResponse(weekService.createWeek(request));
    }

    @Deprecated
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekResponse updateWeek(WeekUpdateRequest request) {
        return WeekMapper.entityToResponse(weekService.updateWeek(request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public SimpleListResponse<WeekResponse> batchUpdateWeeks(BatchUpdateRequest<WeekUpdateRequest> request) {
        return WeekMapper.entityListToSimpleListResponse(weekService.batchUpdateWeeks(request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public Long deleteWeek(Long id) {
        return weekService.deleteWeek(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekResponse triggerCompleted(Long id) {
        return WeekMapper.entityToResponse(weekService.triggerCompleted(id));
    }
}
