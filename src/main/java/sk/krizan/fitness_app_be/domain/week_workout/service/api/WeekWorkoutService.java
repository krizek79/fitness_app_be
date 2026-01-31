package sk.krizan.fitness_app_be.domain.week_workout.service.api;

import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;

public interface WeekWorkoutService {

    PageResponse<WeekWorkoutResponse> filterWeekWorkouts(WeekWorkoutFilterRequest request);

    WeekWorkout getWeekWorkoutById(Long id);

    WeekWorkout createWeekWorkout(WeekWorkoutCreateRequest request);

    WeekWorkout updateWeekWorkout(Long id, WeekWorkoutUpdateRequest request);

    Long deleteWeekWorkout(Long id);

    WeekWorkout triggerCompleted(Long id);
}
