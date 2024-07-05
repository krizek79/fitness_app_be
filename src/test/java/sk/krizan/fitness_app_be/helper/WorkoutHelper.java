package sk.krizan.fitness_app_be.helper;

import org.junit.jupiter.api.Assertions;
import org.springframework.data.domain.Sort;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Level;

import java.util.Set;

public class WorkoutHelper {

    private static final String DEFAULT_VALUE = "default";
    private static final String DEFAULT_UPDATE_VALUE = "update";

    public static WorkoutFilterRequest createFilterRequest() {
        return WorkoutFilterRequest.builder()
                .page(0)
                .size(2)
                .sortBy(Workout.Fields.id)
                .sortDirection(Sort.Direction.DESC.name())
                .build();
    }

    public static WorkoutCreateRequest createCreateRequest() {
        return WorkoutCreateRequest.builder()
                .name(DEFAULT_VALUE)
                .build();
    }

    public static WorkoutUpdateRequest createUpdateRequest(Long id) {
        return WorkoutUpdateRequest.builder()
                .name(DEFAULT_UPDATE_VALUE)
                .description(DEFAULT_UPDATE_VALUE)
                .levelKey(Level.ADVANCED.getKey())
                .tagNames(Set.of(DEFAULT_UPDATE_VALUE))
                .build();
    }

    public static void assertWorkoutSimpleResponse(WorkoutSimpleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(DEFAULT_VALUE, response.name());
    }

    public static void assertWorkoutDetailResponse(WorkoutDetailResponse workout) {
        Assertions.assertNotNull(workout);
        Assertions.assertNotNull(workout.id());
        Assertions.assertNotNull(workout.authorName());
    }
}
