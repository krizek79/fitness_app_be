package sk.krizan.fitness_app_be.domain.week.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekResponse;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekMapper {

    public static WeekResponse entityToResponse(Week week) {
        return WeekResponse.builder()
                .id(week.getId())
                .planId(week.getPlan() != null ? week.getPlan().getId() : null)
                .order(week.getOrder())
                .completed(week.getCompleted())
                .note(week.getNote())
                .numberOfWorkouts(week.getWeekWorkouts().size())
                .numberOfCompletedWorkouts((int) week.getWeekWorkouts().stream().filter(WeekWorkout::getCompleted).count())
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
