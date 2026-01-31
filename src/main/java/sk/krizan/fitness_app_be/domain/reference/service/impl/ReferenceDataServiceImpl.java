package sk.krizan.fitness_app_be.domain.reference.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.domain.reference.entity.BaseEnum;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;
import sk.krizan.fitness_app_be.domain.reference.service.api.ReferenceDataService;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.cycle.entity.Level;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExerciseType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReferenceDataServiceImpl implements ReferenceDataService {

    private static final String ERROR_ENUM_NOT_FOUND = "Enum with key { %s } does not exist.";

    private final Map<String, Class<? extends BaseEnum>> registry = Map.of(
            "levels", Level.class,
            "muscle-groups", MuscleGroup.class,
            "weight-units", WeightUnit.class,
            "workout-exercise-types", WorkoutExerciseType.class,
            "workout-exercise-set-types", WorkoutExerciseSetType.class
    );

    @Override
    public List<String> getAvailableTypes() {
        return registry.keySet().stream().sorted().toList();
    }

    @Override
    public List<ReferenceDataResponse> getReferenceData(String type) {
        Class<? extends BaseEnum> enumClass = registry.get(type.toLowerCase());

        if (enumClass == null) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, "Reference type not supported: " + type);
        }

        return getEnumsOfType(enumClass);
    }

    private List<ReferenceDataResponse> getEnumsOfType(Class<? extends BaseEnum> enumClass) {
        BaseEnum[] constants = enumClass.getEnumConstants();
        if (constants == null) return List.of();

        return Arrays.stream(constants)
                .map(ReferenceDataMapper::enumToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public <T extends Enum<T> & BaseEnum> T findEnumByKey(Class<T> enumClass, String key) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, ERROR_ENUM_NOT_FOUND.formatted(key)));
    }
}
