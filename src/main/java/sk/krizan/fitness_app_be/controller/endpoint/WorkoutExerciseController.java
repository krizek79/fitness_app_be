package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("workout-exercises")
public class WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(@Valid @RequestBody WorkoutExerciseFilterRequest request) {
        return workoutExerciseService.filterWorkoutExercises(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutExerciseResponse getWorkoutExerciseById(@PathVariable Long id) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.getWorkoutExerciseById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutExerciseResponse createWorkoutExercise(@Valid @RequestBody WorkoutExerciseCreateRequest request) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.createWorkoutExercise(request));
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutExerciseResponse updateWorkoutExercise(@Valid @RequestBody WorkoutExerciseUpdateRequest request) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.updateWorkoutExercise(request));
    }

    @PutMapping("batch-update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public SimpleListResponse<WorkoutExerciseResponse> batchUpdateWorkoutExercises(@Valid @RequestBody BatchUpdateRequest<WorkoutExerciseUpdateRequest> request) {
        return WorkoutExerciseMapper.entityListToSimpleListResponse(workoutExerciseService.batchUpdateWorkoutExercises(request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteWorkoutExercise(@PathVariable Long id) {
        return workoutExerciseService.deleteWorkoutExercise(id);
    }
}
