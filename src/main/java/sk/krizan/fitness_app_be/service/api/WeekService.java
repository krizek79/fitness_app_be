package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Week;

public interface WeekService {

    PageResponse<WeekResponse> filterWeeks(WeekFilterRequest request);
    Week getWeekById(Long id);
    Week createWeek(WeekCreateRequest request);
    Week updateWeek(Long id, WeekUpdateRequest request);
    Long deleteWeek(Long id);
    Week triggerCompleted(Long id);
}
