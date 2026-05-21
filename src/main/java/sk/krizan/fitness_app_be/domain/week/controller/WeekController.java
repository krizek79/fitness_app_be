package sk.krizan.fitness_app_be.domain.week.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
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

}
