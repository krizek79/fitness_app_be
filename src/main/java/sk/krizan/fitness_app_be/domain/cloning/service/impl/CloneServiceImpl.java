package sk.krizan.fitness_app_be.domain.cloning.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.domain.cloning.CloneOrchestrator;
import sk.krizan.fitness_app_be.domain.cloning.service.api.CloneService;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.week_workout.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.domain.cycle.repository.CycleRepository;
import sk.krizan.fitness_app_be.domain.week_workout.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.domain.cycle.service.api.CycleService;
import sk.krizan.fitness_app_be.domain.week.service.api.WeekService;
import sk.krizan.fitness_app_be.domain.workout.service.api.WorkoutService;

@Service
@Transactional
@RequiredArgsConstructor
public class CloneServiceImpl implements CloneService {

    private final WeekService weekService;
    private final CycleService cycleService;
    private final WorkoutService workoutService;
    private final CloneOrchestrator cloneOrchestrator;

    private final CycleRepository cycleRepository;
    private final WeekWorkoutRepository weekWorkoutRepository;

    @Override
    public Cycle cloneCycle(Long cycleId) {
        Cycle originalCycle = cycleService.getCycleById(cycleId);
        Cycle clonedCycle = cloneOrchestrator.deepClone(originalCycle);
        return cycleRepository.save(clonedCycle);
    }

    @Override
    public WeekWorkout cloneWorkoutToWeekWorkout(WeekWorkoutCreateRequest request) {
        Week week = weekService.getWeekById(request.weekId());
        Workout originalWorkout = workoutService.getWorkoutById(request.workoutId());

        Workout clonedWorkout = cloneOrchestrator.deepClone(originalWorkout);
        WeekWorkout weekWorkout = WeekWorkoutMapper.createRequestToEntity(request, week, clonedWorkout);

        return weekWorkoutRepository.save(weekWorkout);
    }
}
