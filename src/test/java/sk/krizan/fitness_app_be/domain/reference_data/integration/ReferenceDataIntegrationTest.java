package sk.krizan.fitness_app_be.domain.reference_data.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.domain.cycle.entity.Level;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExerciseType;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;

import java.util.List;

class ReferenceDataIntegrationTest extends BaseIntegrationTest {

    private static final String BASE_URL = "/reference-data";

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAvailableReferenceTypes() throws Exception {
        List<String> availableTypes = performGet(
                BASE_URL,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        org.assertj.core.api.Assertions.assertThat(availableTypes)
                .containsExactlyInAnyOrder(
                        "levels",
                        "muscle-groups",
                        "weight-units",
                        "workout-exercise-types",
                        "workout-exercise-set-types"
                );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getReferenceData_NotFound() throws Exception {
        performGet(
                BASE_URL + "/non-existing-type",
                new TypeReference<>() {
                },
                HttpStatus.NOT_FOUND);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutLevels() throws Exception {
        List<ReferenceDataResponse> responseList = filter("levels");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(Level.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMuscleGroups() throws Exception {
        List<ReferenceDataResponse> responseList = filter("muscle-groups");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(MuscleGroup.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeightUnits() throws Exception {
        List<ReferenceDataResponse> responseList = filter("weight-units");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(WeightUnit.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseTypes() throws Exception {
        List<ReferenceDataResponse> responseList = filter("workout-exercise-types");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(WorkoutExerciseType.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseSetTypes() throws Exception {
        List<ReferenceDataResponse> responseList = filter("workout-exercise-set-types");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(WorkoutExerciseSetType.class, responseList);
    }

    private List<ReferenceDataResponse> filter(String type) throws Exception {
        return performGet(
                BASE_URL + "/" + type,
                new TypeReference<>() {
                },
                HttpStatus.OK);
    }

}
