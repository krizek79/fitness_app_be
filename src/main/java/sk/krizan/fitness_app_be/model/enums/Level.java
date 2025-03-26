package sk.krizan.fitness_app_be.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level implements BaseEnum {

    BEGINNER("BEGINNER", "Beginner"),
    INTERMEDIATE("INTERMEDIATE", "Intermediate"),
    ADVANCED("ADVANCED", "Advanced");

    private final String key;
    private final String value;
}
