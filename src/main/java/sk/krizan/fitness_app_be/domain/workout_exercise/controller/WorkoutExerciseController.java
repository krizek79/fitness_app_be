package sk.krizan.fitness_app_be.domain.workout_exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.common.rest.dto.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.rest.dto.response.SimpleListResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise.service.api.WorkoutExerciseService;

@RestController
@RequiredArgsConstructor
public class WorkoutExerciseController implements sk.krizan.fitness_app_be.domain.workout_exercise.rest.api.WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(WorkoutExerciseFilterRequest request) {
        return workoutExerciseService.filterWorkoutExercises(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseResponse getWorkoutExerciseById(Long id) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.getWorkoutExerciseById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseResponse createWorkoutExercise(WorkoutExerciseCreateRequest request) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.createWorkoutExercise(request));
    }

    @Deprecated
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public WorkoutExerciseResponse updateWorkoutExercise(WorkoutExerciseUpdateRequest request) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.updateWorkoutExercise(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public SimpleListResponse<WorkoutExerciseResponse> batchUpdateWorkoutExercises(BatchUpdateRequest<WorkoutExerciseUpdateRequest> request) {
        return WorkoutExerciseMapper.entityListToSimpleListResponse(workoutExerciseService.batchUpdateWorkoutExercises(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Override
    public Long deleteWorkoutExercise(Long id) {
        return workoutExerciseService.deleteWorkoutExercise(id);
    }
}
