package sk.krizan.fitness_app_be.domain.week.service.api;

import sk.krizan.fitness_app_be.common.rest.dto.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.week.entity.Week;

import java.util.List;

public interface WeekService {

    PageResponse<WeekResponse> filterWeeks(WeekFilterRequest request);

    Week getWeekById(Long id);

    Week createWeek(WeekCreateRequest request);

    Week updateWeek(WeekUpdateRequest request);

    List<Week> batchUpdateWeeks(BatchUpdateRequest<WeekUpdateRequest> request);

    Long deleteWeek(Long id);

    Week triggerCompleted(Long id);
}
