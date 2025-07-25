package sk.krizan.fitness_app_be.helper;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;
import sk.krizan.fitness_app_be.model.entity.Workout;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekWorkoutHelper {

    public static WeekWorkout createMockWeekWorkout(Week week, Workout workout, Integer dayOfTheWeek) {
        WeekWorkout weekWorkout = new WeekWorkout();
        weekWorkout.setWeek(week);
        weekWorkout.setWorkout(workout);
        weekWorkout.setDayOfTheWeek(dayOfTheWeek);

        week.addToWeekWorkoutList(List.of(weekWorkout));

        return weekWorkout;
    }

    public static WeekWorkoutCreateRequest createCreateRequest(Long weekId, Long workoutId, Integer dayOfTheWeek) {
        return WeekWorkoutCreateRequest.builder()
                .weekId(weekId)
                .workoutId(workoutId)
                .dayOfTheWeek(dayOfTheWeek)
                .build();
    }

    public static WeekWorkoutUpdateRequest createUpdateRequest(Integer dayOfTheWeek) {
        return WeekWorkoutUpdateRequest.builder()
                .dayOfTheWeek(dayOfTheWeek)
                .build();
    }

    public static WeekWorkoutFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
            Long weekId
    ) {
        return WeekWorkoutFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .weekId(weekId)
                .build();
    }

    public static void assertFilter(
            List<WeekWorkout> expectedList,
            WeekWorkoutFilterRequest request,
            PageResponse<WeekWorkoutResponse> response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getPageNumber());
        Assertions.assertNotNull(response.getPageSize());
        Assertions.assertNotNull(response.getTotalElements());
        Assertions.assertNotNull(response.getTotalPages());
        Assertions.assertNotNull(response.getResults());
        Assertions.assertFalse(response.getResults().isEmpty());
        Assertions.assertEquals(request.page(), response.getPageNumber());
        Assertions.assertEquals(expectedList.size(), response.getResults().size());

        List<WeekWorkoutResponse> results = response.getResults();
        results.sort(Comparator.comparingLong(WeekWorkoutResponse::id));
        expectedList.sort(Comparator.comparingLong(WeekWorkout::getId));
        for (int i = 0; i < results.size(); i++) {
            WeekWorkoutResponse weekWorkoutResponse = results.get(i);
            WeekWorkout weekWorkout = expectedList.get(i);
            assertWeekWorkoutResponse_get(weekWorkout, weekWorkoutResponse);
        }
    }

    public static void assertWeekWorkoutResponse_get(WeekWorkout weekWorkout, WeekWorkoutResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(weekWorkout.getId(), response.id());
        Assertions.assertEquals(weekWorkout.getWeek().getId(), response.weekId());
        Assertions.assertEquals(weekWorkout.getWorkout().getId(), response.workoutId());
        Assertions.assertEquals(weekWorkout.getWorkout().getName(), response.workoutName());
        Assertions.assertEquals(weekWorkout.getDayOfTheWeek(), response.dayOfTheWeek());
        Assertions.assertEquals(weekWorkout.getCompleted(), response.completed());
        Assertions.assertNotNull(response.workoutTagResponseList());
    }

    public static void assertWeekWorkout_create(WeekWorkoutCreateRequest request, WeekWorkoutResponse response, WeekWorkout weekWorkout) {
        Assertions.assertNotNull(weekWorkout.getId());
        Assertions.assertNotNull(weekWorkout.getWeek());
        Assertions.assertEquals(request.weekId(), weekWorkout.getWeek().getId());
        Assertions.assertNotNull(weekWorkout.getWorkout());
        Assertions.assertEquals(request.workoutId(), weekWorkout.getWorkout().getId());
        Assertions.assertEquals(request.dayOfTheWeek(), weekWorkout.getDayOfTheWeek());
        Assertions.assertNotNull(weekWorkout.getCompleted());
        Assertions.assertFalse(weekWorkout.getCompleted());
        assertWeekWorkoutResponse_get(weekWorkout, response);
    }

    public static void assertWeekWorkout_update(WeekWorkoutUpdateRequest request, WeekWorkoutResponse response, WeekWorkout weekWorkout) {
        Assertions.assertNotNull(weekWorkout.getId());
        Assertions.assertNotNull(weekWorkout.getWeek());
        Assertions.assertNotNull(weekWorkout.getWeek().getId());
        Assertions.assertNotNull(weekWorkout.getWorkout());
        Assertions.assertNotNull(weekWorkout.getWorkout().getId());
        Assertions.assertEquals(request.dayOfTheWeek(), weekWorkout.getDayOfTheWeek());
        Assertions.assertNotNull(weekWorkout.getCompleted());
        Assertions.assertFalse(weekWorkout.getCompleted());
        assertWeekWorkoutResponse_get(weekWorkout, response);
    }

    public static void assertTriggerCompleted(Boolean originalState, WeekWorkoutResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(!originalState, response.completed());
    }

    public static void assertDelete(boolean exists, WeekWorkout weekWorkout, Long deletedWeekWorkoutId) {
        assertFalse(exists);
        assertEquals(weekWorkout.getId(), deletedWeekWorkoutId);
    }
}
