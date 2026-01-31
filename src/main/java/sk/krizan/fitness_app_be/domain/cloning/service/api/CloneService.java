package sk.krizan.fitness_app_be.domain.cloning.service.api;

import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;

public interface CloneService {

    Cycle cloneCycle(Long cycleId);

    WeekWorkout cloneWorkoutToWeekWorkout(WeekWorkoutCreateRequest request);
}
