package sk.krizan.fitness_app_be.domain.week_workout.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.week_workout.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.domain.week_workout.service.api.WeekWorkoutService;

@RestController
@RequiredArgsConstructor
public class WeekWorkoutController implements sk.krizan.fitness_app_be.domain.week_workout.rest.api.WeekWorkoutController {

    private final WeekWorkoutService weekWorkoutService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<WeekWorkoutResponse> filterWeekWorkouts(WeekWorkoutFilterRequest request) {
        return weekWorkoutService.filterWeekWorkouts(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WeekWorkoutResponse createWeekWorkout(WeekWorkoutInputRequest request) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.createUpdateWeekWorkout(null, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WeekWorkoutResponse updateWeekWorkout(Long id, WeekWorkoutInputRequest request) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.createUpdateWeekWorkout(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public void deleteWeekWorkout(Long id) {
        weekWorkoutService.deleteWeekWorkout(id);
    }

}
