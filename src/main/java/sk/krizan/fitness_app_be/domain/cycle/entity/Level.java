package sk.krizan.fitness_app_be.domain.cycle.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum Level implements BaseEnum {

    BEGINNER("BEGINNER", "Beginner"),
    INTERMEDIATE("INTERMEDIATE", "Intermediate"),
    ADVANCED("ADVANCED", "Advanced");

    private final String key;
    private final String value;
}
