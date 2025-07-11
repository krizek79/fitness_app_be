package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.model.enums.BaseEnum;

import java.util.List;

public interface EnumService {

    List<EnumResponse> getWorkoutLevels();

    List<EnumResponse> getMuscleGroups();

    List<EnumResponse> getWeightUnits();

    List<EnumResponse> getWorkoutExerciseTypes();

    List<EnumResponse> getWorkoutExerciseSetTypes();

    <T extends Enum<T> & BaseEnum> T findEnumByKey(Class<T> enumClass, String key);
}
