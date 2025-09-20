package sk.krizan.fitness_app_be.controller.api;

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
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.helper.EnumHelper;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseType;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class EnumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutLevels() throws Exception {
        List<EnumResponse> responseList = filter("workout-levels");
        EnumHelper.assertEnumResponsesMatch(Level.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMuscleGroups() throws Exception {
        List<EnumResponse> responseList = filter("muscle-groups");
        EnumHelper.assertEnumResponsesMatch(MuscleGroup.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeightUnits() throws Exception {
        List<EnumResponse> responseList = filter("weight-units");
        EnumHelper.assertEnumResponsesMatch(WeightUnit.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseTypes() throws Exception {
        List<EnumResponse> responseList = filter("workout-exercise-types");
        EnumHelper.assertEnumResponsesMatch(WorkoutExerciseType.class, responseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseSetTypes() throws Exception {
        List<EnumResponse> responseList = filter("workout-exercise-set-types");
        EnumHelper.assertEnumResponsesMatch(WorkoutExerciseSetType.class, responseList);
    }

    private List<EnumResponse> filter(String urlSuffix) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/enums/" + urlSuffix)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
    }
}
