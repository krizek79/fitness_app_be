package sk.krizan.fitness_app_be.service.impl;

import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.model.enums.BaseEnum;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.service.api.EnumService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnumServiceImpl implements EnumService {

    private static final String ERROR_ENUM_NOT_FOUND = "Enum with key { %s } does not exist.";

    @Override
    public List<EnumResponse> getWorkoutLevels() {
        return getEnumsOfType(Level.class);
    }

    @Override
    public List<EnumResponse> getMuscleGroups() {
        return getEnumsOfType(MuscleGroup.class);
    }

    @Override
    public BaseEnum findEnumByKey(String key) {
        //  Enum types must be declared here
        List<Class<? extends BaseEnum>> enumClasses = Arrays.asList(
            Level.class,
            MuscleGroup.class
        );

        return enumClasses.stream()
            .flatMap(enumClass -> Arrays.stream(enumClass.getEnumConstants()))
            .filter(baseEnum -> baseEnum.getKey().equals(key))
            .findFirst().orElseThrow(() -> new NotFoundException(ERROR_ENUM_NOT_FOUND.formatted(key)));
    }

    private List<EnumResponse> getEnumsOfType(Class<? extends BaseEnum> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(
            baseEnum -> EnumResponse.builder()
                .key(baseEnum.getKey())
                .value(baseEnum.getValue())
                .build()
        ).collect(Collectors.toList());
    }
}
