package sk.krizan.fitness_app_be.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
