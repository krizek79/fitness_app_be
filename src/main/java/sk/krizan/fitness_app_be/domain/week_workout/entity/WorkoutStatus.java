package sk.krizan.fitness_app_be.domain.week_workout.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum WorkoutStatus implements BaseEnum {

    NOT_STARTED("NOT_STARTED", "Not started"),
    IN_PROGRESS("IN_PROGRESS", "In progress"),
    COMPLETED("COMPLETED", "Completed"),
    SKIPPED("SKIPPED", "Skipped");

    private final String key;
    private final String value;
}
