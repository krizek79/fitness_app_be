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
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.event.EntityLifeCycleEventEnum;
import sk.krizan.fitness_app_be.event.EntityReorderEvent;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.model.mapper.WorkoutExerciseSetMapper;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseSetRepository;
import sk.krizan.fitness_app_be.service.api.EnumService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseSetService;
import sk.krizan.fitness_app_be.specification.WorkoutExerciseSetSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseSetServiceImpl implements WorkoutExerciseSetService {

    private final UserService userService;
    private final EnumService enumService;
    private final WorkoutExerciseService workoutExerciseService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final WorkoutExerciseSetRepository workoutExerciseSetRepository;

    private final static String ERROR_WORKOUT_EXERCISE_SET_NOT_FOUND = "WorkoutExerciseSet with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            WorkoutExerciseSet.Fields.id,
            WorkoutExerciseSet.Fields.order
    );

    @Override
    @Transactional
    public PageResponse<WorkoutExerciseSetResponse> filterWorkoutExerciseSets(WorkoutExerciseSetFilterRequest request) {
        Specification<WorkoutExerciseSet> specification = WorkoutExerciseSetSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<WorkoutExerciseSet> page = workoutExerciseSetRepository.findAll(specification, pageable);
        List<WorkoutExerciseSetResponse> responseList = page.stream()
                .map(WorkoutExerciseSetMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<WorkoutExerciseSetResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public WorkoutExerciseSet getWorkoutExerciseSetById(Long id) {
        return workoutExerciseSetRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_WORKOUT_EXERCISE_SET_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public WorkoutExerciseSet createWorkoutExerciseSet(WorkoutExerciseSetCreateRequest request) {
        WorkoutExercise workoutExercise = workoutExerciseService.getWorkoutExerciseById(request.workoutExerciseId());

        WorkoutExerciseSetType workoutExerciseSetType = null;
        if (request.workoutExerciseSetTypeKey() != null) {
            workoutExerciseSetType = enumService.findEnumByKey(WorkoutExerciseSetType.class, request.workoutExerciseSetTypeKey());
        }

        WorkoutExerciseSet workoutExerciseSet = workoutExerciseSetRepository.save(WorkoutExerciseSetMapper.createRequestToEntity(request, workoutExercise, workoutExerciseSetType));
        applicationEventPublisher.publishEvent(new EntityReorderEvent(workoutExerciseSet, EntityLifeCycleEventEnum.CREATE));
        return workoutExerciseSet;
    }

    @Override
    @Transactional
    public WorkoutExerciseSet updateWorkoutExerciseSet(WorkoutExerciseSetUpdateRequest request) {
        WorkoutExerciseSet workoutExerciseSet = getWorkoutExerciseSetById(request.id());

        checkAuthorization(workoutExerciseSet);

        int originalOrder = workoutExerciseSet.getOrder();
        WorkoutExerciseSetType workoutExerciseSetType = null;
        if (request.workoutExerciseSetTypeKey() != null) {
            workoutExerciseSetType = enumService.findEnumByKey(WorkoutExerciseSetType.class, request.workoutExerciseSetTypeKey());
        }

        workoutExerciseSet = workoutExerciseSetRepository.save(WorkoutExerciseSetMapper.updateRequestToEntity(request, workoutExerciseSet, workoutExerciseSetType));
        applicationEventPublisher.publishEvent(new EntityReorderEvent(workoutExerciseSet, EntityLifeCycleEventEnum.UPDATE, originalOrder));
        return workoutExerciseSet;
    }

    @Override
    @Transactional
    public List<WorkoutExerciseSet> batchUpdateWorkoutExerciseSets(BatchUpdateRequest<WorkoutExerciseSetUpdateRequest> request) {
        List<WorkoutExerciseSet> updatedWorkoutExerciseSets = request.updateRequestList().stream()
                .map(this::updateWorkoutExerciseSet)
                .toList();

        return workoutExerciseSetRepository.saveAll(updatedWorkoutExerciseSets);
    }

    @Override
    public Long deleteWorkoutExerciseSet(Long id) {
        WorkoutExerciseSet workoutExerciseSet = getWorkoutExerciseSetById(id);

        checkAuthorization(workoutExerciseSet);

        workoutExerciseSet.getWorkoutExercise().removeFromWorkoutExerciseSetList(workoutExerciseSet);
        workoutExerciseSetRepository.delete(workoutExerciseSet);
        applicationEventPublisher.publishEvent(new EntityReorderEvent(workoutExerciseSet, EntityLifeCycleEventEnum.DELETE));

        return workoutExerciseSet.getId();

    }

    @Override
    @Transactional
    public WorkoutExerciseSet triggerCompleted(Long id) {
        WorkoutExerciseSet workoutExerciseSet = getWorkoutExerciseSetById(id);

        checkAuthorization(workoutExerciseSet);

        workoutExerciseSet.setCompleted(!workoutExerciseSet.getCompleted());
        return workoutExerciseSetRepository.save(workoutExerciseSet);
    }

    private void checkAuthorization(WorkoutExerciseSet workoutExerciseSet) {
        User currentUser = userService.getCurrentUser();

        boolean isTemplate = workoutExerciseSet.getWorkoutExercise().getWorkout().getIsTemplate();
        User workoutAuthor = workoutExerciseSet.getWorkoutExercise().getWorkout().getAuthor().getUser();
        User workoutTrainee = workoutExerciseSet.getWorkoutExercise().getWorkout().getTrainee().getUser();
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
