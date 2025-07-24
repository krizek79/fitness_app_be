package sk.krizan.fitness_app_be.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.ApplicationException;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.model.enums.BaseEnum;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseType;
import sk.krizan.fitness_app_be.model.mapper.EnumMapper;
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
    public List<EnumResponse> getWeightUnits() {
        return getEnumsOfType(WeightUnit.class);
    }

    @Override
    public List<EnumResponse> getWorkoutExerciseTypes() {
        return getEnumsOfType(WorkoutExerciseType.class);
    }

    @Override
    public List<EnumResponse> getWorkoutExerciseSetTypes() {
        return getEnumsOfType(WorkoutExerciseSetType.class);
    }

    @Override
    public <T extends Enum<T> & BaseEnum> T findEnumByKey(Class<T> enumClass, String key) {
        if (!enumClass.isEnum()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Provided class is not an enum: " + enumClass.getName());
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, ERROR_ENUM_NOT_FOUND.formatted(key)));
    }

    private List<EnumResponse> getEnumsOfType(Class<? extends BaseEnum> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(EnumMapper::enumToResponse)
                .collect(Collectors.toList());
    }
}
