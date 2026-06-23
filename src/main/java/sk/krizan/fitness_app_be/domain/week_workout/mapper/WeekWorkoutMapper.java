package sk.krizan.fitness_app_be.domain.week_workout.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.mapper.WorkoutMapper;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekWorkoutMapper {

    public static WeekWorkoutResponse entityToResponse(WeekWorkout weekWorkout) {
        return WeekWorkoutResponse.builder()
                .id(weekWorkout.getId())
                .weekId(weekWorkout.getWeek() != null ? weekWorkout.getWeek().getId() : null)
                .workout(weekWorkout.getWorkout() != null ? WorkoutMapper.entityToSimpleResponse(weekWorkout.getWorkout()) : null)
                .dayOfWeek(weekWorkout.getDayOfWeek())
                .orderInTheDay(weekWorkout.getOrderInTheDay())
                .status(weekWorkout.getStatus())
                .build();
    }

    public static WeekWorkout inputRequestToEntity(
            WeekWorkout weekWorkout,
            WeekWorkoutInputRequest request,
            Week newWeek,
            Workout workout
    ) {
        if (weekWorkout == null) {
            weekWorkout = new WeekWorkout();
            newWeek.addToWeekWorkouts(weekWorkout);
        } else {
            //  This may never happen, but it's better to be safe than sorry. If the week workout already has a week assigned and it's different from the new week, we need to update the associations.
            Week originalWeek = weekWorkout.getWeek();
            if (originalWeek != null && !originalWeek.equals(newWeek)) {
                originalWeek.removeFromWeekWorkouts(weekWorkout);
                newWeek.addToWeekWorkouts(weekWorkout);
            }
        }

        weekWorkout.setWorkout(workout);
        workout.setWeekWorkout(weekWorkout);

        weekWorkout.setDayOfWeek(request.dayOfWeek());
        weekWorkout.setOrderInTheDay(request.orderInTheDay());
        if (request.status() != null) {
            weekWorkout.setStatus(request.status());
        }

        return weekWorkout;
    }

}
