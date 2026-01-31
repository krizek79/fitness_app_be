package sk.krizan.fitness_app_be.domain.reference_data.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;
import sk.krizan.fitness_app_be.domain.cycle.entity.Level;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExerciseType;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ReferenceDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAvailableReferenceTypes() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/reference-data")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<String> availableTypes = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

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
        mockMvc.perform(
                        get("/reference-data/non-existing-type")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
        MvcResult mvcResult = mockMvc.perform(
                        get("/reference-data/" + type)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
    }
}
