package sk.krizan.fitness_app_be.domain.week_workout.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;

import java.time.DayOfWeek;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WeekWorkoutHelper {

    public static WeekWorkout createWeekWorkout(Week week, Workout workout, DayOfWeek dayOfWeek) {
        WeekWorkout weekWorkout = new WeekWorkout();
        weekWorkout.setWorkout(workout);
        weekWorkout.setDayOfWeek(dayOfWeek);
        week.addToWeekWorkouts(weekWorkout);

        return weekWorkout;
    }

    public static WeekWorkoutInputRequest createInputRequest(
            Long weekId,
            Long workoutToCloneId,
            Long workoutToUpdateId,
            WorkoutInputRequest workout,
            DayOfWeek dayOfWeek,
            Integer orderInTheDay,
            Boolean completed
    ) {
        return WeekWorkoutInputRequest.builder()
                .weekId(weekId)
                .workoutToCloneId(workoutToCloneId)
                .workoutToUpdateId(workoutToUpdateId)
                .workout(workout)
                .dayOfWeek(dayOfWeek)
                .orderInTheDay(orderInTheDay)
                .completed(completed)
                .build();
    }

    public static WeekWorkoutFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
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

    public static void assertWeekWorkoutResponse(WeekWorkout weekWorkout, WeekWorkoutResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(weekWorkout.getId(), response.id());
        Assertions.assertEquals(weekWorkout.getWeek().getId(), response.weekId());
        Assertions.assertEquals(weekWorkout.getDayOfWeek(), response.dayOfWeek());
        Assertions.assertEquals(weekWorkout.getCompleted(), response.completed());

        WorkoutHelper.assertWorkoutSimpleResponse(weekWorkout.getWorkout(), response.workout());
    }

    public static void assertDelete(
            boolean weekWorkoutExists,
            boolean workoutExists,
            boolean workoutExerciseExists,
            boolean workoutExerciseSetExists
    ) {
        Assertions.assertFalse(weekWorkoutExists);
        Assertions.assertFalse(workoutExists);
        Assertions.assertFalse(workoutExerciseExists);
        Assertions.assertFalse(workoutExerciseSetExists);
    }

    public static void assertInputToEntity(
            WeekWorkout weekWorkout,
            WeekWorkoutInputRequest request,
            Workout workoutToClone
    ) {
        Assertions.assertNotNull(weekWorkout);
        Assertions.assertEquals(request.weekId(), weekWorkout.getWeek().getId());
        Assertions.assertEquals(request.dayOfWeek(), weekWorkout.getDayOfWeek());
        Assertions.assertEquals(request.completed(), weekWorkout.getCompleted());

        if ((request.workoutToUpdateId() != null && request.workout() != null)
                || (request.workoutToUpdateId() == null && request.workout() != null)
        ) {
            //  If workoutToUpdateId is provided, the workout in the request should be used to update the existing workout. If workoutToUpdateId is not provided, the workout in the request should be used to create a new workout.
            WorkoutHelper.assertInputToEntity(weekWorkout.getWorkout(), request.workout());
        }

        if (request.workoutToCloneId() != null) {
            Assertions.assertNotNull(workoutToClone);
            // If workoutToCloneId is provided, new workout should be created based on the workout with the id provided in workoutToCloneId.
            WorkoutHelper.assertClone(workoutToClone, weekWorkout.getWorkout());
            Assertions.assertEquals(weekWorkout.getWeek().getPlan().getTrainee().getId(), weekWorkout.getWorkout().getTrainee().getId());
        }
    }

}
