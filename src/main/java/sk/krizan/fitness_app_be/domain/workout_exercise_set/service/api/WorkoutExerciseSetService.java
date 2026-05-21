package sk.krizan.fitness_app_be.domain.workout_exercise_set.service.api;

import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;

public interface WorkoutExerciseSetService {

    /**
     * Creates a new workout exercise set or updates an existing one based on the provided input request. If the input request contains an identifier for an existing workout exercise set, the method will update that set with the new information. If no identifier is provided, a new workout exercise set will be created and associated with the specified workout exercise.
     *
     * @param workoutExercise the workout exercise to which the workout exercise set will be associated. This parameter is required for both creating a new workout exercise set and updating an existing one, as it establishes the relationship between the workout exercise set and its parent workout exercise.
     * @param workoutExerciseSetInputRequest the input request containing the data for creating or updating the workout exercise set. This request may include attributes such as the order of the set, the number of repetitions, the weight used, and other relevant information needed to create or update the workout exercise set.
     */
    void createOrUpdateWorkoutExerciseSet(WorkoutExercise workoutExercise, WorkoutExerciseSetInputRequest workoutExerciseSetInputRequest);
}
