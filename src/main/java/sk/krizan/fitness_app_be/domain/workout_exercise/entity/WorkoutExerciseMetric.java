package sk.krizan.fitness_app_be.domain.workout_exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum WorkoutExerciseMetric implements BaseEnum {

    REPS("REPS", "Repetitions"),
    TIME("TIME", "Time"),
    REPS_AND_WEIGHT("REPS_AND_WEIGHT", "Repetitions and Weight"),
    TIME_AND_WEIGHT("TIME_AND_WEIGHT", "Time and Weight"),
    DISTANCE("DISTANCE", "Distance"),
    DISTANCE_AND_TIME("DISTANCE_AND_TIME", "Distance and Time"),;

    private final String key;
    private final String value;
    
}
