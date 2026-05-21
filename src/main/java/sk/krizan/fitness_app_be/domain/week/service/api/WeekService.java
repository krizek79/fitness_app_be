package sk.krizan.fitness_app_be.domain.week.service.api;

import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;

public interface WeekService {

    PageResponse<WeekResponse> filterWeeks(WeekFilterRequest request);

    Week getWeekById(Long id);

    void createUpdateWeek(Cycle cycle, WeekInputRequest weekInputRequest);

}
