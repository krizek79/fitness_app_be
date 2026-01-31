package sk.krizan.fitness_app_be.domain.cloning.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.domain.cycle.mapper.CycleMapper;
import sk.krizan.fitness_app_be.domain.week_workout.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.domain.cloning.service.api.CloneService;

@RestController
@RequiredArgsConstructor
public class CloneController implements sk.krizan.fitness_app_be.domain.cloning.rest.api.CloneController {

    private final CloneService cloneService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public CycleResponse cloneCycle(Long id) {
        return CycleMapper.entityToResponse(cloneService.cloneCycle(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WeekWorkoutResponse cloneWorkoutToWeekWorkout(WeekWorkoutCreateRequest request) {
        return WeekWorkoutMapper.entityToResponse(cloneService.cloneWorkoutToWeekWorkout(request));
    }
}
