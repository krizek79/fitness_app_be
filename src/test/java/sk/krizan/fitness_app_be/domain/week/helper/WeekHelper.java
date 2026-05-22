package sk.krizan.fitness_app_be.domain.week.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WeekHelper {

    public static Week createWeek(Integer order) {
        Week week = new Week();
        week.setOrder(order);
        week.setNote(UUID.randomUUID().toString());

        return week;
    }

    public static WeekInputRequest createInputRequest(
            Long id,
            Integer order,
            String note,
            Boolean completed
    ) {
        return WeekInputRequest.builder()
                .id(id)
                .order(order)
                .note(note)
                .completed(completed)
                .build();
    }

    public static WeekFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long planId
    ) {
        return WeekFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .planId(planId)
                .build();
    }

    public static void assertWeekResponse(Week week, WeekResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(week.getId(), response.id());
        Assertions.assertEquals(week.getPlan().getId(), response.planId());
        Assertions.assertEquals(week.getOrder(), response.order());
        Assertions.assertFalse(response.completed());
        Assertions.assertEquals(week.getNote(), response.note());
        Assertions.assertEquals(week.getWeekWorkouts().size(), response.numberOfWorkouts());
    }

    public static void assertInputToEntity(Week week, WeekInputRequest request) {
        Assertions.assertNotNull(week);

        if (request.id() != null) {
            Assertions.assertEquals(request.id(), week.getId());
        } else {
            Assertions.assertNotNull(week.getId());
        }

        Assertions.assertEquals(request.note(), week.getNote());
        Assertions.assertEquals(request.completed(), week.getCompleted());

        //  questionable assert as order might be different due to user input and reordering in service layer
        Assertions.assertEquals(request.order(), week.getOrder());
    }

}
