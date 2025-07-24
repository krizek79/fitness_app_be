package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.service.api.WeekWorkoutService;

@RestController
@RequiredArgsConstructor
public class WeekWorkoutController implements sk.krizan.fitness_app_be.controller.endpoint.api.WeekWorkoutController {

    private final WeekWorkoutService weekWorkoutService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public PageResponse<WeekWorkoutResponse> filterWeekWorkouts(WeekWorkoutFilterRequest request) {
        return weekWorkoutService.filterWeekWorkouts(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekWorkoutResponse getWeekWorkoutById(Long id) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.getWeekWorkoutById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekWorkoutResponse createWeekWorkout(WeekWorkoutCreateRequest request) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.createWeekWorkout(request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekWorkoutResponse updateWeekWorkout(Long id, WeekWorkoutUpdateRequest request) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.updateWeekWorkout(id, request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public Long deleteWeekWorkout(Long id) {
        return weekWorkoutService.deleteWeekWorkout(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public WeekWorkoutResponse triggerCompleted(Long id) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.triggerCompleted(id));
    }
}
