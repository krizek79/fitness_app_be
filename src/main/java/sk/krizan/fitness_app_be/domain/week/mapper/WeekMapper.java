package sk.krizan.fitness_app_be.domain.week.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekDetailResponse;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekSimpleResponse;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.mapper.WeekWorkoutMapper;

import java.util.Comparator;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekMapper {

    public static WeekSimpleResponse entityToSimpleResponse(Week week) {
        return WeekSimpleResponse.builder()
                .id(week.getId())
                .planId(week.getPlan() != null ? week.getPlan().getId() : null)
                .order(week.getOrder())
                .completed(week.getCompleted())
                .numberOfWorkouts(week.getWeekWorkouts().size())
                .numberOfCompletedWorkouts((int) week.getWeekWorkouts().stream().filter(WeekWorkout::getCompleted).count())
                .build();
    }

    public static WeekDetailResponse entityToDetailResponse(Week week) {
        return WeekDetailResponse.builder()
                .id(week.getId())
                .planId(week.getPlan() != null ? week.getPlan().getId() : null)
                .order(week.getOrder())
                .note(week.getNote())
                .completed(week.getCompleted())
                .weekWorkouts(week.getWeekWorkouts().stream()
                        .sorted(Comparator
                                .comparing(WeekWorkout::getDayOfWeek)
                                .thenComparing(WeekWorkout::getOrderInTheDay))
                        .map(WeekWorkoutMapper::entityToResponse)
                        .toList())
                .build();
    }

    public static void inputRequestToEntity(Week week, WeekInputRequest weekInputRequest, Plan plan) {
        if (week == null) {
            week = new Week();
            plan.addToWeeks(week);
        }

        week.setNote(weekInputRequest.note());
        //  week completed status is set in WeekWorkoutService when all workouts in the week are completed
        //  week order is set in PlanService
    }
}
