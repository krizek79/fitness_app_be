package sk.krizan.fitness_app_be.service.api;

import java.util.List;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.model.enums.BaseEnum;

public interface EnumService {

    List<EnumResponse> getWorkoutLevels();
    List<EnumResponse> getMuscleGroups();
    BaseEnum findEnumByKey(String key);
}
