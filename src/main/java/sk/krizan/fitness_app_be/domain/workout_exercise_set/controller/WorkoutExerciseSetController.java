package sk.krizan.fitness_app_be.domain.workout_exercise_set.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.SimpleListResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.mapper.WorkoutExerciseSetMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.service.api.WorkoutExerciseSetService;

@RestController
@RequiredArgsConstructor
public class WorkoutExerciseSetController implements sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.api.WorkoutExerciseSetController {

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
