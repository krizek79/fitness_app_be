package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Week;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekMapper {

    public static WeekResponse entityToResponse(Week week) {
        return WeekResponse.builder()
                .id(week.getId())
                .cycleId(week.getCycle() != null ? week.getCycle().getId() : null)
                .order(week.getOrder())
                .completed(week.getCompleted())
                .build();
    }

    public static Week createRequestToEntity(WeekCreateRequest request, Cycle cycle) {
        Week week = new Week();
        week.setCycle(cycle);
        week.setOrder(request.order());
        cycle.addToWeekList(List.of(week));
        return week;
    }

    public static Week updateRequestToEntity(WeekUpdateRequest request, Week week) {
        week.setOrder(request.order());
        return week;
    }

    public static SimpleListResponse<WeekResponse> entityListToSimpleListResponse(List<Week> weekList) {
        return SimpleListResponse.<WeekResponse>builder()
                .result(weekList.stream().map(WeekMapper::entityToResponse).toList())
                .build();
    }
}
