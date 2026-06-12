package sk.krizan.fitness_app_be.domain.reference.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DistanceUnit implements BaseEnum {

    KM("KM", "Kilometers"),
    MILES("MILES", "Miles");

    private final String key;
    private final String value;

}
