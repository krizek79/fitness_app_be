package sk.krizan.fitness_app_be.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.WorkoutMapper;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.EnumService;
import sk.krizan.fitness_app_be.service.api.TagService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;
import sk.krizan.fitness_app_be.specification.WorkoutSpecification;
import sk.krizan.fitness_app_be.util.PageUtils;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final UserService userService;
    private final EnumService enumService;
    private final TagService tagService;

    private final WorkoutRepository workoutRepository;

    private static final String ERROR_NOT_FOUND = "Workout with id { %s } does not exist.";

    //  TODO: add author name and level
    private static final List<String> supportedSortFields = List.of(
        "id",
        "name"
    );

    @Override
    public PageResponse<WorkoutResponse> filterWorkouts(
        WorkoutFilterRequest request
    ) {
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
            .map(WorkoutMapper::entityToResponse).toList();

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
        return workoutRepository.findById(id).orElseThrow(
            () -> new NotFoundException(ERROR_NOT_FOUND.formatted(id)));
    }

    @Override
    public Workout createWorkout(WorkoutCreateRequest request) {
        Profile profile = userService.getCurrentUser().getProfile();
        return workoutRepository.save(WorkoutMapper.createRequestToEntity(request, profile));
    }

    @Override
    public Workout updateWorkoutLevel(Long id, List<String> tagNames) {
        User currentUser = userService.getCurrentUser();
        Workout workout = getWorkoutById(id);

        if (workout.getAuthor().getUser() != currentUser
            && !currentUser.getRoles().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        List<Tag> tags = tagNames.stream()
            .map(tagName -> tagService.findTagByName(tagName.toLowerCase())
                .orElseGet(() -> tagService.createTag(new TagCreateRequest(tagName))))
            .collect(Collectors.toList());

        workout.setTags(tags);

        return workoutRepository.save(workout);
    }

    @Override
    public Workout updateWorkoutLevel(Long id, String levelKey) {
        User currentUser = userService.getCurrentUser();
        Workout workout = getWorkoutById(id);

        if (workout.getAuthor().getUser() != currentUser
            && !currentUser.getRoles().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        Level level = (Level) enumService.findEnumByKey(levelKey);

        workout.setLevel(level);

        return workoutRepository.save(workout);
    }

    @Override
    public Long deleteWorkout(Long id) {
        User currentUser = userService.getCurrentUser();
        Workout workout = getWorkoutById(id);

        if (workout.getAuthor().getUser() != currentUser
            && !currentUser.getRoles().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        workoutRepository.delete(workout);
        return workout.getId();
    }
}
