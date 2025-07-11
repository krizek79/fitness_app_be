package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;

public interface CloneService {

    Cycle cloneCycle(Long cycleId);

    WeekWorkout cloneWorkoutToWeekWorkout(WeekWorkoutCreateRequest request);
}
