package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.event.EntityLifeCycleEventEnum;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseType;
import sk.krizan.fitness_app_be.model.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.service.api.EnumService;
import sk.krizan.fitness_app_be.service.api.ExerciseService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;
import sk.krizan.fitness_app_be.specification.WorkoutExerciseSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final UserService userService;
    private final EnumService enumService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final String ERROR_NOT_FOUND = "WorkoutExercise with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            WorkoutExercise.Fields.id,
            WorkoutExercise.Fields.order
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
        WorkoutExerciseType workoutExerciseType = enumService.findEnumByKey(WorkoutExerciseType.class, request.workoutExerciseTypeKey());
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseMapper.createRequestToEntity(request, workout, exercise, workoutExerciseType));
        applicationEventPublisher.publishEvent(new EntityReorderEvent(workoutExercise, EntityLifeCycleEventEnum.CREATE));
        return workoutExercise;
    }

    @Override
    @Transactional
    public WorkoutExercise updateWorkoutExercise(WorkoutExerciseUpdateRequest request) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(request.id());
        WorkoutExerciseType workoutExerciseType = enumService.findEnumByKey(WorkoutExerciseType.class, request.workoutExerciseTypeKey());

        checkAuthorization(workoutExercise);

        int originalOrder = workoutExercise.getOrder();
        workoutExercise = workoutExerciseRepository.save(WorkoutExerciseMapper.updateRequestToEntity(workoutExercise, workoutExerciseType, request));
        applicationEventPublisher.publishEvent(new EntityReorderEvent(workoutExercise, EntityLifeCycleEventEnum.UPDATE, originalOrder));

        return workoutExercise;
    }

    @Override
    public List<WorkoutExercise> batchUpdateWorkoutExercises(BatchUpdateRequest<WorkoutExerciseUpdateRequest> request) {
        List<WorkoutExercise> updatedWorkoutExercises = request.updateRequestList().stream()
                .sorted(Comparator.comparing(WorkoutExerciseUpdateRequest::order))
                .map(this::updateWorkoutExercise)
                .toList();

        return workoutExerciseRepository.saveAll(updatedWorkoutExercises);
    }

    @Override
    public Long deleteWorkoutExercise(Long id) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);

        checkAuthorization(workoutExercise);

        workoutExercise.getWorkout().removeFromWorkoutExerciseList(workoutExercise);
        workoutExerciseRepository.delete(workoutExercise);
        applicationEventPublisher.publishEvent(new EntityReorderEvent(workoutExercise, EntityLifeCycleEventEnum.DELETE));

        return workoutExercise.getId();
    }

    private void checkAuthorization(WorkoutExercise workoutExercise) {
        User currentUser = userService.getCurrentUser();

        Workout workout = workoutExercise.getWorkout();
        boolean isTemplate = workout.getIsTemplate();
        User workoutAuthor = workout.getAuthor().getUser();
        User workoutTrainee = workout.getTrainee().getUser();
        boolean isAdmin = currentUser.getRoleSet().contains(Role.ADMIN);

        // Only trainee or admin can access non-template workouts
        boolean unauthorizedNonTemplateAccess = !isTemplate && !workoutTrainee.equals(currentUser) && !isAdmin;

        // Only author or admin can access template workouts
        boolean unauthorizedTemplateAccess = isTemplate && !workoutAuthor.equals(currentUser) && !isAdmin;

        // Non-template workout must have the same author and trainee
        boolean invalidAuthorOwnerCombination = !isTemplate && !workoutAuthor.equals(workoutTrainee);

        if (unauthorizedTemplateAccess || unauthorizedNonTemplateAccess || invalidAuthorOwnerCombination) {
            throw new ForbiddenException();
        }
    }
}
