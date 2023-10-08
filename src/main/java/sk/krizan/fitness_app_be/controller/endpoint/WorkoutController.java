package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
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
    public PageResponse<WorkoutResponse> filterWorkouts(
        @Valid WorkoutFilterRequest request
    ) {
        return workoutService.filterWorkouts(request);
    }

    @GetMapping("{id}")
    public WorkoutResponse getWorkoutById(@PathVariable Long id) {
        return WorkoutMapper.entityToResponse(workoutService.getWorkoutById(id));
    }

    @PostMapping
    public WorkoutResponse createWorkout(@Valid @RequestBody WorkoutCreateRequest request) {
        return WorkoutMapper.entityToResponse(workoutService.createWorkout(request));
    }

    @PutMapping("{id}/tags")
    public WorkoutResponse updateWorkoutTags(
        @PathVariable Long id,
        @RequestBody List<String> tagNames
    ) {
        return WorkoutMapper.entityToResponse(workoutService.updateTags(id, tagNames));
    }

    @PatchMapping("{id}/level/{levelKey}")
    public WorkoutResponse updateWorkoutLevel(
        @PathVariable Long id,
        @PathVariable String levelKey
    ) {
        return WorkoutMapper.entityToResponse(workoutService.updateLevel(id, levelKey));
    }

    @DeleteMapping("{id}")
    public Long deleteWorkout(@PathVariable Long id) {
        return workoutService.deleteWorkout(id);
    }
}
