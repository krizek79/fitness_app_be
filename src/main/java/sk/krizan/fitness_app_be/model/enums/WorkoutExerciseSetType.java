package sk.krizan.fitness_app_be.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkoutExerciseSetType implements BaseEnum {

    WARMUP("WARMUP", "Warm-up set"),
    TOP_SET("TOP_SET", "Top set"),
    BACKOFF_SET("BACKOFF_SET", "Back-off set"),
    STRAIGHT_SET("STRAIGHT_SET", "Straight set");

    private final String key;
    private final String value;
}
