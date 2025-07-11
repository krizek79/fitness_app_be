package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;

public interface WeekWorkoutService {

    PageResponse<WeekWorkoutResponse> filterWeekWorkouts(WeekWorkoutFilterRequest request);

    WeekWorkout getWeekWorkoutById(Long id);

    WeekWorkout createWeekWorkout(WeekWorkoutCreateRequest request);

    WeekWorkout updateWeekWorkout(Long id, WeekWorkoutUpdateRequest request);

    Long deleteWeekWorkout(Long id);

    WeekWorkout triggerCompleted(Long id);
}
