package sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum Muscle implements BaseEnum {

    CHEST("CHEST", "Chest"),
    UPPER_CHEST("UPPER_CHEST", "Upper chest"),
    LOWER_CHEST("LOWER_CHEST", "Lower chest"),

    SHOULDERS("SHOULDERS", "Shoulders"),
    FRONT_DELTS("FRONT_DELTS", "Front delts"),
    REAR_DELTS("REAR_DELTS", "Rear delts"),
    LATERAL_DELTS("LATERAL_DELTS", "Lateral delts"),

    BACK("BACK", "Back"),
    UPPER_BACK("UPPER_BACK", "Upper back"),
    LATS("LATS", "Latissimus dorsi"),
    TRAPS("TRAPS", "Trapezius"),
    LOWER_BACK("LOWER_BACK", "Lower back"),

    BICEPS("BICEPS", "Biceps"),
    TRICEPS("TRICEPS", "Triceps"),
    FOREARMS("FOREARMS", "Forearms"),

    ABS("ABS", "Abs"),
    OBLIQUES("OBLIQUES", "Obliques"),

    GLUTES("GLUTES", "Glutes"),
    QUADS("QUADS", "Quadriceps"),
    HAMSTRINGS("HAMSTRINGS", "Hamstrings"),
    CALVES("CALVES", "Calves"),
    HIP_FLEXORS("HIP_FLEXORS", "Hip flexors"),

    NECK("NECK", "Neck");

    private final String key;
    private final String value;

}
