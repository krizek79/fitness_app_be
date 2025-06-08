package sk.krizan.fitness_app_be.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeightUnit implements BaseEnum {

    KG("KG", "kg"),
    LB("LB", "lb");

    private final String key;
    private final String value;
}
