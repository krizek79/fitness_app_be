package sk.krizan.fitness_app_be.domain.week_workout.service.api;

import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;

public interface WeekWorkoutService {

    /**
     * Filters week workouts based on the provided filter request.
     * The filter can include criteria such as week ID, workout ID, and pagination parameters.
     * The method returns a paginated response containing the filtered week workouts.
     *
     * @param request filter request for week workouts
     * @return page response with filtered week workouts
     */
    PageResponse<WeekWorkoutResponse> filterWeekWorkouts(WeekWorkoutFilterRequest request);

    /**
     * Creates a new week workout or updates an existing one.
     * The method supports creating a new workout, cloning an existing workout, or updating an existing workout.
     * After creation or update, the week's completed status is recalculated based on the completion status of all its week workouts.
     *
     * @param id the ID of the week workout to update, or null to create a new one
     * @param request the input request containing week workout details and workout information
     * @return the created or updated WeekWorkout entity
     * @throws sk.krizan.fitness_app_be.common.exception.ApplicationException if the week is not found, user does not have permission to create/update, or if the update fails
     */
    WeekWorkout createUpdateWeekWorkout(Long id, WeekWorkoutInputRequest request);

    /**
     * Deletes the week workout with the given ID.
     *
     * @param id ID of the week workout to delete
     * @throws sk.krizan.fitness_app_be.common.exception.ApplicationException if the week workout is not found or user does not have a permission to delete it
     */
    void deleteWeekWorkout(Long id);

}
