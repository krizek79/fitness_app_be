package sk.krizan.fitness_app_be.domain.workout_exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum WorkoutExerciseType implements BaseEnum {

    WEIGHT("WEIGHT", "weight"),
    WEIGHT_TIME("WEIGHT_TIME", "weight-time"),
    TIME("TIME", "time"),
    BODYWEIGHT("BODYWEIGHT", "bodyweight");

    private final String key;
    private final String value;
}
