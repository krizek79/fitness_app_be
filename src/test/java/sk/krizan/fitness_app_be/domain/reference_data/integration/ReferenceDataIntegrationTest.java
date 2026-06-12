package sk.krizan.fitness_app_be.domain.reference_data.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.domain.exercise.entity.ExerciseCategory;
import sk.krizan.fitness_app_be.domain.exercise.entity.MovementPattern;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;
import sk.krizan.fitness_app_be.domain.reference.entity.DistanceUnit;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExerciseMetric;
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
                        "weight-units",
                        "distance-units",
                        "workout-exercise-types",
                        "workout-exercise-set-types",
                        "muscles",
                        "movement-patterns",
                        "exercise-category"
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
    void getMuscles() throws Exception {
        List<ReferenceDataResponse> responseList = filter("muscles");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(Muscle.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeightUnits() throws Exception {
        List<ReferenceDataResponse> responseList = filter("weight-units");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(WeightUnit.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDistanceUnits() throws Exception {
        List<ReferenceDataResponse> responseList = filter("distance-units");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(DistanceUnit.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseTypes() throws Exception {
        List<ReferenceDataResponse> responseList = filter("workout-exercise-types");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(WorkoutExerciseMetric.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseSetTypes() throws Exception {
        List<ReferenceDataResponse> responseList = filter("workout-exercise-set-types");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(WorkoutExerciseSetType.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getExerciseCategories() throws Exception {
        List<ReferenceDataResponse> responseList = filter("exercise-category");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(ExerciseCategory.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMovementPatterns() throws Exception {
        List<ReferenceDataResponse> responseList = filter("movement-patterns");
        ReferenceDataHelper.assertReferenceDataResponsesMatch(MovementPattern.class, responseList);
    }

    private List<ReferenceDataResponse> filter(String type) throws Exception {
        return performGet(
                BASE_URL + "/" + type,
                new TypeReference<>() {
                },
                HttpStatus.OK);
    }

}
