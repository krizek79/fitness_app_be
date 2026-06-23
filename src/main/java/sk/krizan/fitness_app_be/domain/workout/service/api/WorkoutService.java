package sk.krizan.fitness_app_be.domain.workout.service.api;

import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;

public interface WorkoutService {

    /**
     * Retrieves a paginated list of workouts based on the specified filter criteria.
     *
     * @param request the request containing the filter criteria for retrieving workouts. The request may include parameters such as page number, page size, sorting options, and various filters based on workout attributes.
     * @return a paginated response containing a list of workouts that match the specified filter criteria. The response includes metadata about the pagination, such as total pages, total elements, and the current page number.
     */
    PageResponse<WorkoutSimpleResponse> filterWorkouts(WorkoutFilterRequest request);

    /**
     * Retrieves the workout with the specified ID.
     *
     * @param id ID of the workout to be retrieved
     * @return the workout with the specified ID
     * @throws ApplicationException if the workout with the specified ID does not exist or the user
     */
    Workout getWorkoutById(Long id);

    /**
     * Creates a new workout or updates an existing workout based on the provided ID and request data.
     * This service also serves as a gateway for handling workout exercises and workout exercise sets.
     *
     * @param id ID of the workout to be updated. If null, a new workout will be created.
     * @param request the request containing the data for creating or updating the workout.
     * @return the created or updated workout
     * @throws ApplicationException if the workout with the specified ID does not exist (when updating) or the user is not authorized to update the workout.
     */
    Workout createUpdateWorkout(Long id, WorkoutInputRequest request);

    /**
     * Deletes the workout with the specified ID.
     * If the workout does not exist, an exception is thrown.
     * If the workout is associated with any workout exercises,
     * those associations are also removed to maintain data integrity.
     *
     * @param id ID of the workout to be deleted
     * @throws ApplicationException if the workout with the specified ID does not exist or the user is not authorized to delete it
     */
    void deleteWorkout(Long id);


}
