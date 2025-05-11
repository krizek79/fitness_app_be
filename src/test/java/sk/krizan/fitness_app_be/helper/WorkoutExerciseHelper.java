package sk.krizan.fitness_app_be.helper;

import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.time.Duration;
import java.util.List;

public class WorkoutExerciseHelper {

    public static void createMockWorkoutExercise(Workout workout, Exercise exercise, int sets, int repetitions, Duration restDuration) {
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .exercise(exercise)
                .workout(workout)
                .sets(sets)
                .repetitions(repetitions)
                .restDuration(restDuration)
                .build();
        workout.addToWorkoutExerciseList(List.of(workoutExercise));
    }
}
