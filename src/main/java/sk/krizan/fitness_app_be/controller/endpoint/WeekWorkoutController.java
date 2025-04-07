package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("week-workouts")
public class WeekWorkoutController {

    private final WeekWorkoutService weekWorkoutService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<WeekWorkoutResponse> filterWeekWorkouts(@Valid @RequestBody WeekWorkoutFilterRequest request) {
        return weekWorkoutService.filterWeekWorkouts(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekWorkoutResponse getWeekWorkoutById(@PathVariable Long id) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.getWeekWorkoutById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekWorkoutResponse createWeekWorkout(@Valid @RequestBody WeekWorkoutCreateRequest request) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.createWeekWorkout(request));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekWorkoutResponse updateWeekWorkout(
            @PathVariable Long id,
            @Valid @RequestBody WeekWorkoutUpdateRequest request
    ) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.updateWeekWorkout(id, request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteWeekWorkout(@PathVariable Long id) {
        return weekWorkoutService.deleteWeekWorkout(id);
    }

    @PutMapping("{id}/trigger-completed")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekWorkoutResponse triggerCompleted(@PathVariable Long id) {
        return WeekWorkoutMapper.entityToResponse(weekWorkoutService.triggerCompleted(id));
    }
}
