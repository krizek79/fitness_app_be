package sk.krizan.fitness_app_be.domain.week.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.week.mapper.WeekMapper;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekDetailResponse;
import sk.krizan.fitness_app_be.domain.week.service.api.WeekService;

@RestController
@RequiredArgsConstructor
public class WeekController implements sk.krizan.fitness_app_be.domain.week.rest.api.WeekController {

    private final WeekService weekService;

    @Override
    @PreAuthorize("hasPermission(#id, 'WEEK', 'READ')")
    public WeekDetailResponse getWeekById(Long id) {
        return WeekMapper.entityToDetailResponse(weekService.getWeekById(id));
    }

}
