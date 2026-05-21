package sk.krizan.fitness_app_be.domain.workout.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.cloning.CloneOrchestrator;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.coach_client.service.api.CoachClientService;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.tag.service.api.TagService;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.mapper.WorkoutMapper;
import sk.krizan.fitness_app_be.domain.workout.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout.service.api.WorkoutService;
import sk.krizan.fitness_app_be.domain.workout.specification.WorkoutSpecification;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.service.api.WorkoutExerciseService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final TagService tagService;
    private final UserService userService;
    private final CloneOrchestrator cloneOrchestrator;
    private final CoachClientService coachClientService;
    private final WorkoutExerciseService workoutExerciseService;

    private final WorkoutRepository workoutRepository;

    private static final List<String> supportedSortFields = List.of(
            Workout.Fields.id,
            Workout.Fields.title
    );

    /**
     * Retrieves a paginated list of workouts based on the specified filter criteria.
     *
     * @param request the request containing the filter criteria for retrieving workouts. The request may include parameters such as page number, page size, sorting options, and various filters based on workout attributes.
     * @return a paginated response containing a list of workouts that match the specified filter criteria. The response includes metadata about the pagination, such as total pages, total elements, and the current page number.
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<WorkoutSimpleResponse> filterWorkouts(WorkoutFilterRequest request) {
        Specification<Workout> specification = WorkoutSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<Workout> page = workoutRepository.findAll(specification, pageable);
        List<WorkoutSimpleResponse> responseList = page.stream()
                .map(WorkoutMapper::entityToSimpleResponse).collect(Collectors.toList());

        return PageResponse.<WorkoutSimpleResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    /**
     * Retrieves the workout with the specified ID.
     *
     * @param id ID of the workout to be retrieved
     * @return the workout with the specified ID
     * @throws ApplicationException if the workout with the specified ID does not exist or the user
     */
    @Override
    public Workout getWorkoutById(Long id) {
        return workoutRepository.getByIdOrThrow(id);
    }

    /**
     * Creates a new workout or updates an existing workout based on the provided ID and request data.
     * This service also serves as a gateway for handling workout exercises and workout exercise sets.
     *
     * @param id      ID of the workout to be updated. If null, a new workout will be created.
     * @param request the request containing the data for creating or updating the workout.
     * @return the created or updated workout
     * @throws ApplicationException if the workout with the specified ID does not exist (when updating) or the user is not authorized to update the workout.
     */
    @Override
    @Transactional
    public Workout createUpdateWorkout(Long id, WorkoutInputRequest request) {
        Workout workout;
        Profile trainee;
        Profile author;

        User currentUser = userService.getCurrentUser();

        boolean isNew = id == null;
        if (isNew) {
            workout = new Workout();
            author = currentUser.getProfile();
        } else {
            workout = getWorkoutById(id);
            author = workout.getAuthor();
        }

        trainee = coachClientService.resolveTrainee(request.traineeId(), author, isNew ? author : workout.getTrainee());

        WorkoutMapper.inputRequestToEntity(isNew, request, workout, author, trainee);

        resolveWorkoutExercises(workout, request.workoutExercises());
        resolveTags(workout, request.tags());

        return workoutRepository.save(workout);
    }

    /**
     * Resolves the workout exercises for the given workout based on the provided list of workout exercise input requests.
     *
     * @param workout          the workout for which the workout exercises should be resolved.
     * @param workoutExercises the list of workout exercise input requests containing the data for creating or updating the workout exercises.
     */
    private void resolveWorkoutExercises(Workout workout, List<WorkoutExerciseInputRequest> workoutExercises) {
        Set<Long> incomingIds = workoutExercises.stream().map(WorkoutExerciseInputRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        // Remove exercises that are not in the incoming request
        workout.getWorkoutExercises().removeIf(workoutExercise -> workoutExercise.getId() != null && !incomingIds.contains(workoutExercise.getId()));

        // Add or update exercises from the incoming request
        for (WorkoutExerciseInputRequest workoutExerciseInputRequest : workoutExercises) {
            workoutExerciseService.createUpdateWorkoutExercise(workout, workoutExerciseInputRequest);
        }

        //  Ensure the order of workout exercises is consistent with the input request
        workout.getWorkoutExercises().sort(Comparator.comparing(
                WorkoutExercise::getOrder,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));

        int currentOrder = 1;
        for (WorkoutExercise we : workout.getWorkoutExercises()) {
            we.setOrder(currentOrder++);
        }
    }

    /**
     * Resolves the tags for the given workout based on the provided set of tag create requests.
     * The method extracts the tag names from the requests,
     * retrieves or creates the corresponding Tag entities, and then associates those tags with the workout.
     * This ensures that both existing and new tags are properly handled and linked to the workout.
     *
     * @param workout           the workout for which the tags should be resolved
     * @param tagCreateRequests the set of tag create requests containing the names of the tags to be associated with the workout. The method will handle both existing and new tags based on the provided names.
     */
    private void resolveTags(Workout workout, Set<TagCreateRequest> tagCreateRequests) {
        Set<String> tagNames = tagCreateRequests.stream()
                .filter(Objects::nonNull)
                .map(TagCreateRequest::name)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<Tag> tags = tagService.getOrCreateTags(tagNames);

        workout.getTags().clear();
        workout.addToTags(tags);
    }

    /**
     * Deletes the workout with the specified ID.
     * If the workout does not exist, an exception is thrown.
     * If the workout is associated with any workout exercises,
     * those associations are also removed to maintain data integrity.
     *
     * @param id ID of the workout to be deleted
     * @throws ApplicationException if the workout with the specified ID does not exist or the user is not authorized to delete it
     */
    @Override
    @Transactional
    public void deleteWorkout(Long id) {
        Workout workout = getWorkoutById(id);

        workout.getAuthor().removeFromAuthoredWorkouts(workout);
        workout.getTrainee().removeFromAssignedWorkouts(workout);

        workoutRepository.delete(workout);
    }

    /**
     * Creates a new workout that is a clone of an existing workout with the specified ID.
     * The cloned workout will have a new unique ID, and all other attributes will be copied from the original workout.
     * This allows users to easily create variations of existing workouts without having to manually recreate all the details.
     *
     * @param id       ID of the workout to be cloned
     * @return a new workout that is a clone of the workout with the specified ID.
     * @throws ApplicationException if the workout with the specified ID does not exist or the user is not authorized to clone it
     */
    @Override
    @Transactional
    public Workout cloneWorkout(Long id) {
        Workout original = workoutRepository.getByIdOrThrow(id);
        Workout clone = cloneOrchestrator.deepClone(original);

        return workoutRepository.save(clone);
    }

}
