package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.cloning.CloneContext;
import sk.krizan.fitness_app_be.cloning.CloneOrchestrator;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.service.api.CloneService;
import sk.krizan.fitness_app_be.service.api.CycleService;
import sk.krizan.fitness_app_be.service.api.WeekService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;

@Service
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
        Cycle clonedCycle = cloneOrchestrator.deepClone(originalCycle, new CloneContext());
        return cycleRepository.save(clonedCycle);
    }

    @Override
    public WeekWorkout cloneWorkoutToWeekWorkout(WeekWorkoutCreateRequest request) {
        Week week = weekService.getWeekById(request.weekId());
        Workout originalWorkout = workoutService.getWorkoutById(request.workoutId());

        Workout clonedWorkout = cloneOrchestrator.deepClone(originalWorkout, new CloneContext());
        WeekWorkout weekWorkout = WeekWorkoutMapper.createRequestToEntity(request, week, clonedWorkout);

        return weekWorkoutRepository.save(weekWorkout);
    }
}
