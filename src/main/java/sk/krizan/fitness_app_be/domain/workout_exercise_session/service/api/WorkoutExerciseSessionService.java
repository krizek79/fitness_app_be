package sk.krizan.fitness_app_be.domain.workout_exercise_session.service.api;

import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;

public interface WorkoutExerciseSessionService {

    WorkoutExerciseSession getWorkoutExerciseSessionById(Long id);

    void createUpdateWorkoutExerciseSession(WorkoutSession workoutSession, WorkoutExerciseSessionInputRequest request);

}
