package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.mapper.CycleMapper;
import sk.krizan.fitness_app_be.model.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.service.api.CloneService;

@RestController
@RequiredArgsConstructor
public class CloneController implements sk.krizan.fitness_app_be.controller.endpoint.api.CloneController {

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
