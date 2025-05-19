package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.service.api.ExerciseService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;
import sk.krizan.fitness_app_be.specification.WorkoutExerciseSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    private final WorkoutExerciseRepository workoutExerciseRepository;

    private static final String ERROR_NOT_FOUND = "WorkoutExercise with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            WorkoutExercise.Fields.id
    );

    @Override
    @Transactional
    public PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(WorkoutExerciseFilterRequest request) {
        Specification<WorkoutExercise> specification = WorkoutExerciseSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<WorkoutExercise> page = workoutExerciseRepository.findAll(specification, pageable);
        List<WorkoutExerciseResponse> responseList = page.stream()
                .map(WorkoutExerciseMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<WorkoutExerciseResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public WorkoutExercise getWorkoutExerciseById(Long id) {
        return workoutExerciseRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public WorkoutExercise createWorkoutExercise(WorkoutExerciseCreateRequest request) {
        Workout workout = workoutService.getWorkoutById(request.workoutId());
        Exercise exercise = exerciseService.getExerciseById(request.exerciseId());
        return workoutExerciseRepository.save(
            WorkoutExerciseMapper.createRequestToEntity(request, workout, exercise));
    }

    @Override
    @Transactional
    public WorkoutExercise updateWorkoutExercise(WorkoutExerciseUpdateRequest request) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(request.id());
        return workoutExerciseRepository.save(
            WorkoutExerciseMapper.updateRequestToEntity(workoutExercise, request));
    }

    @Override
    public Long deleteWorkoutExercise(Long id) {
        User currentUser = userService.getCurrentUser();
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);

        if (workoutExercise.getWorkout() == null) {
            throw new RuntimeException("Workout is null");
        }

        if (workoutExercise.getWorkout().getAuthor().getUser() != currentUser
            && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        workoutExercise.getWorkout().removeFromWorkoutExerciseList(workoutExercise);
        workoutExerciseRepository.delete(workoutExercise);

        return workoutExercise.getId();
    }
}
