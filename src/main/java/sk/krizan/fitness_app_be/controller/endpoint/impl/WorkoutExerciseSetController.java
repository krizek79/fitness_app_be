package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class WorkoutExerciseSetController implements sk.krizan.fitness_app_be.controller.endpoint.api.WorkoutExerciseSetController {

    private final WorkoutExerciseSetService workoutExerciseSetService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<WorkoutExerciseSetResponse> filterWorkoutExerciseSets(WorkoutExerciseSetFilterRequest request) {
        return workoutExerciseSetService.filterWorkoutExerciseSets(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseSetResponse getWorkoutExerciseSetById(Long id) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.getWorkoutExerciseSetById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseSetResponse createWorkoutExerciseSet(WorkoutExerciseSetCreateRequest request) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.createWorkoutExerciseSet(request));
    }

    @Deprecated
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseSetResponse updateWorkoutExerciseSet(WorkoutExerciseSetUpdateRequest request) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.updateWorkoutExerciseSet(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public SimpleListResponse<WorkoutExerciseSetResponse> batchUpdateWorkoutExerciseSets(BatchUpdateRequest<WorkoutExerciseSetUpdateRequest> request) {
        return WorkoutExerciseSetMapper.entityListToSimpleListResponse(workoutExerciseSetService.batchUpdateWorkoutExerciseSets(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public Long deleteWorkoutExerciseSet(Long id) {
        return workoutExerciseSetService.deleteWorkoutExerciseSet(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseSetResponse triggerCompleted(Long id) {
        return WorkoutExerciseSetMapper.entityToResponse(workoutExerciseSetService.triggerCompleted(id));
    }
}
