package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.WorkoutMapper;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.TagService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;
import sk.krizan.fitness_app_be.specification.WorkoutSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final TagService tagService;
    private final UserService userService;

    private final WorkoutRepository workoutRepository;

    private static final String ERROR_NOT_FOUND = "Workout with id { %s } does not exist.";

    private static final List<String> supportedSortFields = List.of(
            Workout.Fields.id,
            Workout.Fields.name,
            Workout.Fields.author + "." + Profile.Fields.name
    );

    @Override
    @Transactional
    public PageResponse<WorkoutResponse> filterWorkouts(WorkoutFilterRequest request) {
        Specification<Workout> specification = WorkoutSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Workout> page = workoutRepository.findAll(specification, pageable);
        List<WorkoutResponse> responseList = page.stream()
                .map(WorkoutMapper::entityToResponse).collect(Collectors.toList());

        return PageResponse.<WorkoutResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    @Override
    public Workout getWorkoutById(Long id) {
        return workoutRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public Workout createWorkout(WorkoutCreateRequest request) {
        Profile profile = userService.getCurrentUser().getProfile();
        return workoutRepository.save(WorkoutMapper.createRequestToEntity(request, profile));
    }

    @Override
    @Transactional
    public Workout updateWorkout(Long id, WorkoutUpdateRequest request) {
        User currentUser = userService.getCurrentUser();
        Workout workout = getWorkoutById(id);

        if (workout.getAuthor().getUser() != currentUser && !currentUser.getRoleSet().contains(Role.ADMIN)) {
            throw new ForbiddenException();
        }

        Set<Tag> tags = new HashSet<>();
        if (request.tagNames() != null) {
            tags = request.tagNames().stream()
                    .map(tagName -> tagService.findTagByName(tagName.toLowerCase())
                            .orElseGet(() -> tagService.createTag(new TagCreateRequest(tagName))))
                    .collect(Collectors.toSet());
        }

        return workoutRepository.save(WorkoutMapper.updateRequestToEntity(request, workout, tags));
    }

    @Override
    public Long deleteWorkout(Long id) {
        User currentUser = userService.getCurrentUser();
        Workout workout = getWorkoutById(id);

        if (workout.getAuthor().getUser() != currentUser && !currentUser.getRoleSet().contains(Role.ADMIN)) {
            throw new ForbiddenException();
        }

        workout.setDeleted(true);
        workoutRepository.save(workout);
        return workout.getId();
    }
}
