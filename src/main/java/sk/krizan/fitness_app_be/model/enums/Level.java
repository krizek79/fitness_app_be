package sk.krizan.fitness_app_be.model.enums;

import lombok.Getter;

@Getter
public enum Level implements BaseEnum {

    BEGINNER("BEGINNER", "Beginner"),
    INTERMEDIATE("INTERMEDIATE", "Intermediate"),
    ADVANCED("ADVANCED", "Advanced");

    private final String key;
    private final String value;

    Level(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
