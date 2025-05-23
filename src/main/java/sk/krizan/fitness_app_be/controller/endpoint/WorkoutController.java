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
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.mapper.WorkoutMapper;
import sk.krizan.fitness_app_be.service.api.WorkoutService;

@RestController
@RequiredArgsConstructor
@RequestMapping("workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<WorkoutResponse> filterWorkouts(@Valid @RequestBody WorkoutFilterRequest request) {
        return workoutService.filterWorkouts(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutResponse getWorkoutById(@PathVariable Long id) {
        return WorkoutMapper.entityToResponse(workoutService.getWorkoutById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutResponse createWorkout(@Valid @RequestBody WorkoutCreateRequest request) {
        return WorkoutMapper.entityToResponse(workoutService.createWorkout(request));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutResponse updateWorkout(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutUpdateRequest request
    ) {
        return WorkoutMapper.entityToResponse(workoutService.updateWorkout(id, request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteWorkout(@PathVariable Long id) {
        return workoutService.deleteWorkout(id);
    }
}
