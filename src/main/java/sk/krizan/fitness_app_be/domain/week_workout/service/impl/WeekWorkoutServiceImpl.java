package sk.krizan.fitness_app_be.domain.week_workout.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.PageUtils;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.repository.WeekRepository;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.domain.week_workout.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.domain.week_workout.service.api.WeekWorkoutService;
import sk.krizan.fitness_app_be.domain.week_workout.specification.WeekWorkoutSpecification;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.service.api.WorkoutService;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekWorkoutServiceImpl implements WeekWorkoutService {

    private final WorkoutService workoutService;

    private final WeekRepository weekRepository;
    private final WeekWorkoutRepository weekWorkoutRepository;

    private static final List<String> supportedSortFields = List.of(
            WeekWorkout.Fields.id,
            WeekWorkout.Fields.dayOfWeek
    );

    /**
     * Filters week workouts based on the provided filter request.
     * The filter can include criteria such as week ID, workout ID, and pagination parameters.
     * The method returns a paginated response containing the filtered week workouts.
     *
     * @param request filter request for week workouts
     * @return page response with filtered week workouts
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<WeekWorkoutResponse> filterWeekWorkouts(WeekWorkoutFilterRequest request) {
        Specification<WeekWorkout> specification = WeekWorkoutSpecification.filter(request);
        Pageable pageable = PageUtils.createPageable(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                supportedSortFields
        );
        Page<WeekWorkout> page = weekWorkoutRepository.findAll(specification, pageable);
        List<WeekWorkoutResponse> responseList = page.stream()
                .map(WeekWorkoutMapper::entityToResponse)
                .collect(Collectors.toList());

        return PageResponse.<WeekWorkoutResponse>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .results(responseList)
                .build();
    }

    /**
     * Creates a new week workout or updates an existing one.
     * The method supports creating a new workout, cloning an existing workout, or updating an existing workout.
     * After creation or update, the week's completed status is recalculated based on the completion status of all its week workouts.
     *
     * @param id      the ID of the week workout to update, or null to create a new one
     * @param request the input request containing week workout details and workout information
     * @return the created or updated WeekWorkout entity
     * @throws sk.krizan.fitness_app_be.common.exception.ApplicationException if the week is not found, user does not have permission to create/update, or if the update fails
     */
    @Override
    @Transactional
    public WeekWorkout createUpdateWeekWorkout(Long id, WeekWorkoutInputRequest request) {
        Week week = weekRepository.getByIdOrThrow(request.weekId());
        Profile cycleTrainee = week.getCycle().getTrainee();

        Workout workout = handleWorkout(request, cycleTrainee);

        WeekWorkout weekWorkout = handleWeekWorkout(id, request, week, workout);

        synchronizeOrdersForDay(week, request.dayOfWeek());

        handleWeekCompletedStatus(week);

        return weekWorkoutRepository.save(weekWorkout);
    }

    private Workout handleWorkout(WeekWorkoutInputRequest request, Profile cycleTrainee) {
        Workout workout;

        if (request.workoutToUpdateId() != null) {
            //  Update existing workout
            workout = workoutService.createUpdateWorkout(request.workoutToUpdateId(), request.workout());
        } else if (request.workoutToCloneId() != null) {
            //  Clone existing workout
            workout = workoutService.cloneWorkout(request.workoutToCloneId());
            cycleTrainee.addToAssignedWorkouts(workout);
        } else {
            //  Create new workout
            workout = workoutService.createUpdateWorkout(null, request.workout());
        }

        return workout;
    }

    private WeekWorkout handleWeekWorkout(Long id, WeekWorkoutInputRequest request, Week week, Workout workout) {
        WeekWorkout weekWorkout;

        boolean isNew = id == null;
        if (isNew) {
            //  Create new week workout
            weekWorkout = WeekWorkoutMapper.inputRequestToEntity(null, request, week, workout);
        } else {
            //  Update existing week workout
            weekWorkout = weekWorkoutRepository.getByIdOrThrow(id);
            weekWorkout = WeekWorkoutMapper.inputRequestToEntity(weekWorkout, request, week, workout);
        }

        return weekWorkout;
    }

    /**
     * Deletes the week workout with the given ID.
     *
     * @param id ID of the week workout to delete
     * @throws sk.krizan.fitness_app_be.common.exception.ApplicationException if the week workout is not found or user does not have a permission to delete it
     */
    @Override
    @Transactional
    public void deleteWeekWorkout(Long id) {
        WeekWorkout weekWorkout = weekWorkoutRepository.getByIdOrThrow(id);

        Workout workout = weekWorkout.getWorkout();
        workout.getAuthor().removeFromAuthoredWorkouts(workout);
        workout.getTrainee().removeFromAssignedWorkouts(workout);

        Week week = weekWorkout.getWeek();
        week.removeFromWeekWorkouts(weekWorkout);

        DayOfWeek dayOfWeek = weekWorkout.getDayOfWeek();
        synchronizeOrdersForDay(week, dayOfWeek);
        handleWeekCompletedStatus(week);

        weekWorkoutRepository.delete(weekWorkout);
    }

    private void handleWeekCompletedStatus(Week week) {
        week.getWeekWorkouts().stream()
                .filter(ww -> !ww.getCompleted())
                .findAny()
                .ifPresentOrElse(
                        w -> week.setCompleted(false),
                        () -> week.setCompleted(true));
    }

    /**
     * Synchronizes the order of workouts for a specific day of the week.
     * Ensures that all workouts on the same day have sequential orderInTheDay values starting from 1.
     *
     * @param week      the week containing the workouts
     * @param dayOfWeek the day to synchronize orders for
     */
    private void synchronizeOrdersForDay(Week week, DayOfWeek dayOfWeek) {
        List<WeekWorkout> workoutsForDay = week.getWeekWorkouts().stream()
                .filter(ww -> ww.getDayOfWeek() == dayOfWeek)
                .sorted(Comparator.comparingInt(WeekWorkout::getOrderInTheDay))
                .toList();

        for (int i = 0; i < workoutsForDay.size(); i++) {
            workoutsForDay.get(i).setOrderInTheDay(i + 1);
        }
    }

}
