package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseSetHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseSetRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class WorkoutExerciseControllerTest {

    @Autowired
    private WorkoutExerciseSetRepository workoutExerciseSetRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createMockProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterWorkoutExercises() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS)));

        Workout workout1 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WorkoutExercise workoutExercise1 = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout1, exercise, 1));

        Workout workout2 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WorkoutExercise workoutExercise2 = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout2, exercise, 1));

        List<WorkoutExercise> originalList = List.of(workoutExercise1, workoutExercise2);

        WorkoutExerciseFilterRequest request = WorkoutExerciseHelper.createFilterRequest(0, originalList.size(), WorkoutExercise.Fields.id, Sort.Direction.DESC.name(), workout2.getId());

        MvcResult mvcResult = mockMvc.perform(
                        post("/workout-exercises/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<WorkoutExerciseResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        List<WorkoutExercise> expectedList = new ArrayList<>(List.of(originalList.get(1)));
        WorkoutExerciseHelper.assertFilter(response, expectedList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseById() throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS)));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));
        WorkoutExerciseSet workoutExerciseSet = workoutExerciseSetRepository.save(WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 1));
        List<WorkoutExerciseSet> expectedWorkoutExerciseSetList = new ArrayList<>(List.of(workoutExerciseSet));

        MvcResult mvcResult = mockMvc.perform(
                        get("/workout-exercises/" + workoutExercise.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WorkoutExerciseResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WorkoutExerciseHelper.assertGet(workoutExercise, response, expectedWorkoutExerciseSetList);
    }

    private static Stream<Arguments> createWorkoutExerciseMethodSource() {
        return Stream.of(
                // Case 1: Create with position 2 -> should insert into position 2, shift WorkoutExercise2 and WorkoutExercise3 to 3 and 4
                Arguments.of(2, 2, List.of(1, 2, 3, 4)),
                // Case 2: Create with position 5 -> beyond current size -> goes to last (4th place)
                Arguments.of(5, 4, List.of(1, 2, 3, 4))
        );
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("createWorkoutExerciseMethodSource")
    void createWorkoutExercise(
            Integer requestedOrder,
            Integer expectedInsertedOrder,
            List<Integer> expectedFinalOrder
    ) throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of()));
        List<WorkoutExercise> originalList = List.of(
                WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1),
                WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 2),
                WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 3)
        );
        workoutExerciseRepository.saveAll(originalList);

        WorkoutExerciseCreateRequest request = WorkoutExerciseHelper.createCreateRequest(workout.getId(), exercise.getId(), requestedOrder);

        MvcResult mvcResult = mockMvc.perform(
                        post("/workout-exercises")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WorkoutExerciseResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        List<WorkoutExercise> finalWorkoutExerciseList = workoutExerciseRepository.findAllByWorkoutIdOrderByOrder(workout.getId());
        WorkoutExerciseHelper.assertCreate(request, response, expectedInsertedOrder, finalWorkoutExerciseList, expectedFinalOrder);
    }

    public static Stream<Arguments> updateWorkoutExerciseMethodSource() {
        return Stream.of(
                // Move WorkoutExercise at position 3 to position 1
                Arguments.of(List.of(1, 2, 3, 4), 2, 1, List.of(2, 0, 1, 3)),
                // Move WorkoutExercise at position 1 to position 3
                Arguments.of(List.of(1, 2, 3, 4), 0, 3, List.of(1, 2, 0, 3)),
                // Move WorkoutExercise at position 4 to position 2
                Arguments.of(List.of(1, 2, 3, 4), 3, 2, List.of(0, 3, 1, 2)),
                // Move WorkoutExercise at position 2 to position 4
                Arguments.of(List.of(1, 2, 3, 4), 1, 4, List.of(0, 2, 3, 1)),
                // Move WorkoutExercise at position 2 to the same position (should result in no reordering)
                Arguments.of(List.of(1, 2, 3), 1, 2, List.of(0, 1, 2))
        );
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("updateWorkoutExerciseMethodSource")
    void updateWorkoutExercise(
            List<Integer> originalOrders,
            Integer idOfElementToUpdate,
            Integer newRequestedOrder,
            List<Integer> listIdsOfExpectedElementsInOrder
    ) throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of()));

        List<WorkoutExercise> originalList = new ArrayList<>();
        for (Integer order : originalOrders) {
            WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, order);
            originalList.add(workoutExerciseRepository.save(workoutExercise));
        }
        Assertions.assertEquals(originalList.size(), listIdsOfExpectedElementsInOrder.size());
        List<Long> idsOfExpectedElementsInOrder = listIdsOfExpectedElementsInOrder.stream()
                .map(index -> originalList.get(index).getId())
                .collect(Collectors.toList());

        WorkoutExercise workoutExerciseToUpdate = originalList.get(idOfElementToUpdate);
        WorkoutExerciseUpdateRequest request = WorkoutExerciseHelper.createUpdateRequest(workoutExerciseToUpdate.getId(), newRequestedOrder);

        MvcResult mvcResult = mockMvc.perform(
                        put("/workout-exercises")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WorkoutExerciseResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        List<WorkoutExercise> finalWorkoutExerciseList = workoutExerciseRepository.findAllByWorkoutIdOrderByOrder(workout.getId());
        WorkoutExerciseHelper.assertUpdate(request, response, idsOfExpectedElementsInOrder, finalWorkoutExerciseList);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWorkoutExercise() throws Exception {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);
        WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1);
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

        MvcResult mvcResult = mockMvc.perform(
                        delete("/workout-exercises/" + workoutExercise.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedWorkoutExerciseId = Long.parseLong(jsonResponse);

        boolean exists = workoutExerciseRepository.existsById(deletedWorkoutExerciseId);

        WorkoutExerciseHelper.assertDelete(exists, workoutExercise, deletedWorkoutExerciseId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void batchUpdateWorkoutExercises() throws Exception {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);

        List<WorkoutExercise> originalList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, i);
            originalList.add(workoutExerciseRepository.save(workoutExercise));
        }

        List<WorkoutExerciseUpdateRequest> requestList = new ArrayList<>();
        for (int i = originalList.size(); i > 0; i--) {
            Long id = originalList.get(originalList.size() - i).getId();
            requestList.add(WorkoutExerciseHelper.createUpdateRequest(id, i));
        }
        BatchUpdateRequest<WorkoutExerciseUpdateRequest> batchRequest = BatchUpdateRequest.<WorkoutExerciseUpdateRequest>builder()
                .updateRequestList(requestList)
                .build();

        MvcResult mvcResult = mockMvc.perform(
                        put("/workout-exercises/batch-update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(batchRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        SimpleListResponse<WorkoutExerciseResponse> listResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WorkoutExerciseHelper.assertBatchUpdate(requestList, listResponse);
    }
}