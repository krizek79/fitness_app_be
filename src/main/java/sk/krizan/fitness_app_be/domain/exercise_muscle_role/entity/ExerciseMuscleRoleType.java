package sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum ExerciseMuscleRoleType implements BaseEnum {

    PRIMARY("PRIMARY", "Primary"),
    SECONDARY("SECONDARY", "Secondary");

    private final String key;
    private final String value;

}
