package sk.krizan.fitness_app_be.domain.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum ExerciseCategory implements BaseEnum {

    CARDIO("CARDIO",  "Cardiology"),
    MOBILITY("MOBILITY",  "Mobility"),
    REHAB("REHAB",  "Rehabilitation"),
    STRENGTH("STRENGTH",  "Strength"),
    STRETCHING("STRETCHING",  "Stretching");

    private final String key;
    private final String value;

}
