package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.TriConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.DefaultValues;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static Stream<Arguments> filterExercisesMethodSource() {
        List<Exercise> originalExerciseList = ExerciseHelper.createOriginalExercises();

        List<Exercise> exerciseListByName = originalExerciseList.stream()
                .filter(exercise -> exercise.getName().equals("Bench press"))
                .collect(Collectors.toList());

        List<MuscleGroup> muscleGroupList = List.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS);
        List<Exercise> exerciseListByMuscleGroupList = originalExerciseList.stream()
                .filter(exercise -> exercise.getMuscleGroupSet().containsAll(muscleGroupList))
                .collect(Collectors.toList());

        return Stream.of(
                Arguments.of(
                        originalExerciseList,
                        ExerciseHelper.createFilterRequest(0, originalExerciseList.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), null, null),
                        (TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>>) ExerciseHelper::assertFilter),
                Arguments.of(
                        exerciseListByName,
                        ExerciseHelper.createFilterRequest(0, exerciseListByName.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), "Bench press", null),
                        (TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>>) ExerciseHelper::assertFilter),
                Arguments.of(
                        exerciseListByMuscleGroupList,
                        ExerciseHelper.createFilterRequest(0, exerciseListByMuscleGroupList.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), null, muscleGroupList),
                        (TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>>) ExerciseHelper::assertFilter)
        );
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("filterExercisesMethodSource")
    void filterExercises(
            List<Exercise> expectedExerciseList,
            ExerciseFilterRequest request,
            TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>> assertion
    ) throws Exception {
        List<Exercise> savedExpectedExerciseList = exerciseRepository.saveAll(expectedExerciseList);

        MvcResult mvcResult = mockMvc.perform(
                        post("/exercises/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<ExerciseResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertion.accept(savedExpectedExerciseList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getExerciseById() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS)));

        MvcResult mvcResult = mockMvc.perform(
                        get("/exercises/" + exercise.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExerciseResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        ExerciseHelper.assertExerciseResponse_get(exercise, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createExercise() throws Exception {
        ExerciseCreateRequest request = ExerciseHelper.createCreateRequest(DefaultValues.DEFAULT_VALUE, Set.of(MuscleGroup.FULL_BODY));

        MvcResult mvcResult = mockMvc.perform(
                        post("/exercises")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExerciseResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        ExerciseHelper.assertExerciseResponse_create(request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteExercise() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS)));

        MvcResult mvcResult = mockMvc.perform(
                        delete("/exercises/" + exercise.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedExerciseId = Long.parseLong(jsonResponse);

        boolean exists = exerciseRepository.existsById(deletedExerciseId);

        ExerciseHelper.assertDelete(exists, exercise, deletedExerciseId);
    }
}
