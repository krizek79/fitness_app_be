package sk.krizan.fitness_app_be.domain.workout_exercise_set_result.service.api;

import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request.WorkoutExerciseSetResultInputRequest;

public interface WorkoutExerciseSetResultService {

    void createUpdateWorkoutExerciseSetResult(WorkoutExerciseSession workoutExerciseSession, WorkoutExerciseSetResultInputRequest request);

}
