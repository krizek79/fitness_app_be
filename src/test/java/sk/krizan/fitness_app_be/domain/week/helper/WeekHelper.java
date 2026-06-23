package sk.krizan.fitness_app_be.domain.week.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekDetailResponse;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekSimpleResponse;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.week_workout.helper.WeekWorkoutHelper;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;

import java.util.Comparator;
import java.util.List;
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
            String note
    ) {
        return WeekInputRequest.builder()
                .id(id)
                .order(order)
                .note(note)
                .build();
    }

    public static void assertWeekSimpleResponse(Week week, WeekSimpleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(week.getId(), response.id());
        Assertions.assertEquals(week.getPlan().getId(), response.planId());
        Assertions.assertEquals(week.getOrder(), response.order());
        Assertions.assertFalse(response.completed());
        Assertions.assertEquals(week.getWeekWorkouts().size(), response.numberOfWorkouts());
        Assertions.assertEquals(Math.toIntExact(week.getWeekWorkouts().stream().filter(ww -> ww.getStatus() == WorkoutStatus.COMPLETED || ww.getStatus() == WorkoutStatus.SKIPPED).count()), response.numberOfCompletedWorkouts());
    }

    public static void assertWeekDetailResponse(Week week, WeekDetailResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(week.getId(), response.id());
        Assertions.assertEquals(week.getPlan().getId(), response.planId());
        Assertions.assertEquals(week.getOrder(), response.order());
        Assertions.assertEquals(week.getNote(), response.note());
        Assertions.assertEquals(week.getCompleted(), response.completed());

        Assertions.assertEquals(week.getWeekWorkouts().size(), response.weekWorkouts().size());
        List<WeekWorkout> sortedWeekWorkouts = week.getWeekWorkouts().stream().sorted(Comparator.comparing(WeekWorkout::getDayOfWeek).thenComparing(WeekWorkout::getOrderInTheDay)).toList();
        for (int i = 0; i < sortedWeekWorkouts.size(); i++) {
            WeekWorkout weekWorkout = sortedWeekWorkouts.get(i);
            WeekWorkoutResponse weekWorkoutResponse = response.weekWorkouts().get(i);
            WeekWorkoutHelper.assertWeekWorkoutResponse(weekWorkout, weekWorkoutResponse);
        }
    }

    public static void assertInputToEntity(Week week, WeekInputRequest request) {
        Assertions.assertNotNull(week);

        if (request.id() != null) {
            Assertions.assertEquals(request.id(), week.getId());
        } else {
            Assertions.assertNotNull(week.getId());
        }

        Assertions.assertEquals(request.note(), week.getNote());

        //  questionable assert as order might be different due to user input and reordering in service layer
        Assertions.assertEquals(request.order(), week.getOrder());
    }

}
