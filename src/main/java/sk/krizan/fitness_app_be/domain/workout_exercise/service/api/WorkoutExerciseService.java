package sk.krizan.fitness_app_be.domain.workout_exercise.service.api;

import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;

public interface WorkoutExerciseService {

    /**
     * Retrieves a workout exercise by its unique identifier.
     *
     * @param id the unique identifier of the workout exercise to be retrieved. This identifier is typically a numeric value that uniquely identifies a specific workout exercise in the system.
     * @return the workout exercise corresponding to the provided identifier. If no workout exercise is found with the given identifier, an appropriate exception may be thrown.
     * @throws ApplicationException if no workout exercise is found with the provided identifier. The exception may include details about the error, such as an error message and an HTTP status code indicating the nature of the error (e.g., 404 Not Found).
     */
    WorkoutExercise getWorkoutExerciseById(Long id);

    /**
     * Creates a new workout exercise or updates an existing one based on the provided input request. If the input request contains an identifier for an existing workout exercise, the method will update the corresponding workout exercise with the new data. If no identifier is provided, a new workout exercise will be created and associated with the specified workout.
     *
     * @param workout the workout to which the workout exercise will be associated. This parameter is typically an instance of the Workout entity that represents the workout to which the exercise belongs.
     * @param workoutExerciseInputRequest the input request containing the data for creating or updating the workout exercise. This request may include attributes such as exercise details, order, and other relevant information needed to create or update the workout exercise.
     */
    void createUpdateWorkoutExercise(Workout workout, WorkoutExerciseInputRequest workoutExerciseInputRequest);

}
