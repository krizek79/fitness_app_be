package sk.krizan.fitness_app_be.model.enums;

import lombok.Getter;

@Getter
public enum MuscleGroup {

    LEGS("Legs"),
    CHEST("Chest"),
    SHOULDERS("Shoulders"),
    BACK("Back"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    ABS("Abs"),
    FULL_BODY("Full body"),
    NECK("Neck");

    public final String value;

    MuscleGroup(String value) {
        this.value = value;
    }
}
