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
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.model.mapper.WorkoutExerciseSetMapper;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseSetService;

@RestController
@RequiredArgsConstructor
@RequestMapping("workout-exercise-sets")
public class WorkoutExerciseSetController {

    private final WorkoutExerciseSetService workoutExerciseSetService;

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PageResponse<WorkoutExerciseSetResponse> filterWorkoutExerciseSets(@Valid @RequestBody WorkoutExerciseSetFilterRequest request) {
        return workoutExerciseSetService.filterWorkoutExerciseSets(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutExerciseSetResponse getWorkoutExerciseSetById(@PathVariable Long id) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.getWorkoutExerciseSetById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutExerciseSetResponse createWorkoutExerciseSet(@Valid @RequestBody WorkoutExerciseSetCreateRequest request) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.createWorkoutExerciseSet(request));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutExerciseSetResponse updateWorkoutExerciseSet(@Valid @RequestBody WorkoutExerciseSetUpdateRequest request) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.updateWorkoutExerciseSet(request));
    }

    @PutMapping("batch-update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public SimpleListResponse<WorkoutExerciseSetResponse> batchUpdateWorkoutExerciseSets(@Valid @RequestBody BatchUpdateRequest<WorkoutExerciseSetUpdateRequest> request) {
        return WorkoutExerciseSetMapper.entityListToSimpleListResponse(workoutExerciseSetService.batchUpdateWorkoutExerciseSets(request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Long deleteWorkoutExerciseSet(@PathVariable Long id) {
        return workoutExerciseSetService.deleteWorkoutExerciseSet(id);
    }

    @PutMapping("{id}/trigger-completed")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WorkoutExerciseSetResponse triggerCompleted(@PathVariable Long id) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.triggerCompleted(id));
    }
}
