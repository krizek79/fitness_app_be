package sk.krizan.fitness_app_be.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MuscleGroup implements BaseEnum {

    LEGS("LEGS", "Legs"),
    CHEST("CHEST", "Chest"),
    SHOULDERS("SHOULDERS", "Shoulders"),
    BACK("BACK", "Back"),
    BICEPS("BICEPS", "Biceps"),
    TRICEPS("TRICEPS", "Triceps"),
    ABS("ABS", "Abs"),
    FULL_BODY("FULL_BODY", "Full body"),
    NECK("NECK", "Neck");

    private final String key;
    private final String value;
}
