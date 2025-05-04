package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.WeekBatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.model.entity.Week;

import java.util.List;

public interface WeekService {

    PageResponse<WeekResponse> filterWeeks(WeekFilterRequest request);
    Week getWeekById(Long id);
    Week createWeek(WeekCreateRequest request);
    Week updateWeek(WeekUpdateRequest request);
    Long deleteWeek(Long id);
    Week triggerCompleted(Long id);
    List<Week> batchUpdateWeeks(WeekBatchUpdateRequest request);
}
