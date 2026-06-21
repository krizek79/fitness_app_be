package sk.krizan.fitness_app_be.domain.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum MovementPattern implements BaseEnum {

    SQUAT("SQUAT", "Squat"),
    HINGE("HINGE", "Hinge"),
    LUNGE("LUNGE", "Lunge"),
    HORIZONTAL_PUSH("HORIZONTAL_PUSH", "Horizontal Push"),
    VERTICAL_PUSH("VERTICAL_PUSH", "Vertical Push"),
    HORIZONTAL_PULL("HORIZONTAL_PULL", "Horizontal Pull"),
    VERTICAL_PULL("VERTICAL_PULL", "Vertical Pull"),
    CARRY("CARRY", "Carry"),
    ROTATION("ROTATION", "Rotation"),
    ANTI_ROTATION("ANTI_ROTATION", "Anti Rotation"),
    ANTI_EXTENSION("ANTI_EXTENSION", "Anti Extension"),
    ANTI_LATERAL_FLEXION("ANTI_LATERAL_FLEXION", "Anti Lateral Flexion"),
    LOCOMOTION("LOCOMOTION", "Locomotion");

    private final String key;
    private final String value;

}
