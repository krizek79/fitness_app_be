package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;
import sk.krizan.fitness_app_be.model.entity.Workout;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekWorkoutMapper {

    public static WeekWorkout createRequestToEntity(WeekWorkoutCreateRequest request, Week week, Workout workout) {
        WeekWorkout weekWorkout = new WeekWorkout();
        weekWorkout.setWeek(week);
        weekWorkout.setWorkout(workout);
        weekWorkout.setDayOfTheWeek(request.dayOfTheWeek());
        week.addToWeekWorkoutList(List.of(weekWorkout));
        return weekWorkout;
    }

    public static WeekWorkoutResponse entityToResponse(WeekWorkout weekWorkout) {
        return WeekWorkoutResponse.builder()
                .id(weekWorkout.getId())
                .weekId(weekWorkout.getWeek() != null ? weekWorkout.getWeek().getId() : null)
                .workoutId(weekWorkout.getWorkout() != null ? weekWorkout.getWorkout().getId() : null)
                .workoutName(weekWorkout.getWorkout() != null ? weekWorkout.getWorkout().getName() : null)
                .workoutTagResponseList(
                        weekWorkout.getWorkout() != null && weekWorkout.getWorkout().getTagSet() != null
                                ? weekWorkout.getWorkout().getTagSet().stream().map(TagMapper::entityToResponse).toList()
                                : new ArrayList<>())
                .dayOfTheWeek(weekWorkout.getDayOfTheWeek())
                .completed(weekWorkout.getCompleted())
                .build();
    }

    public static WeekWorkout updateRequestToEntity(WeekWorkoutUpdateRequest request, WeekWorkout weekWorkout) {
        weekWorkout.setDayOfTheWeek(request.dayOfTheWeek());
        return weekWorkout;
    }
}
