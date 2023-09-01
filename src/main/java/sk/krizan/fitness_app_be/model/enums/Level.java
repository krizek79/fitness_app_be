package sk.krizan.fitness_app_be.model.enums;

public enum Level {

    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    public final String value;

    Level(String value) {
        this.value = value;
    }
}
