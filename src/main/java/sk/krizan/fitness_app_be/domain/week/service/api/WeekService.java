package sk.krizan.fitness_app_be.domain.week.service.api;

import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;

public interface WeekService {

    Week getWeekById(Long id);

    void createUpdateWeek(Plan plan, WeekInputRequest weekInputRequest);

}
