package sk.krizan.fitness_app_be.domain.week.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.SimpleListResponse;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.week.entity.Week;

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
                .note(week.getNote())
                .numberOfWorkouts(week.getWeekWorkoutList().size())
                .build();
    }

    public static Week createRequestToEntity(WeekCreateRequest request, Cycle cycle) {
        Week week = new Week();
        week.setCycle(cycle);
        week.setOrder(request.order());
        week.setNote(request.note());
        cycle.addToWeekList(List.of(week));
        return week;
    }

    public static Week updateRequestToEntity(WeekUpdateRequest request, Week week) {
        week.setOrder(request.order());
        week.setNote(request.note());
        return week;
    }

    public static SimpleListResponse<WeekResponse> entityListToSimpleListResponse(List<Week> weekList) {
        return SimpleListResponse.<WeekResponse>builder()
                .result(weekList.stream().map(WeekMapper::entityToResponse).toList())
                .build();
    }
}
