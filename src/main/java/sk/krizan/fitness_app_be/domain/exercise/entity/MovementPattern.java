package sk.krizan.fitness_app_be.domain.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;

@Getter
@AllArgsConstructor
public enum MovementPattern implements BaseEnum {

    SQUAT("squat", "Squat"),
    HINGE("hinge", "Hinge"),
    LUNGE("lunge", "Lunge"),
    HORIZONTAL_PUSH("horizontal_push", "Horizontal Push"),
    VERTICAL_PUSH("vertical_push", "Vertical Push"),
    HORIZONTAL_PULL("horizontal_pull", "Horizontal Pull"),
    VERTICAL_PULL("vertical_pull", "Vertical Pull"),
    CARRY("carry", "Carry"),
    ROTATION("rotation", "Rotation"),
    ANTI_ROTATION("anti_rotation", "Anti Rotation"),
    ANTI_EXTENSION("anti_extension", "Anti Extension"),
    ANTI_LATERAL_FLEXION("anti_lateral_flexion", "Anti Lateral Flexion"),
    LOCOMOTION("locomotion", "Locomotion");

    private final String key;
    private final String value;

}
