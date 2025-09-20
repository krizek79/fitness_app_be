package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.TagHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.TagRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
class WorkoutControllerTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

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
    void filterWorkouts() throws Exception {
        List<Profile> profileList = new ArrayList<>();
        List<List<WorkoutExercise>> workoutExerciseList = new ArrayList<>();
        List<Set<Tag>> tagSetList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
            Profile profile = profileRepository.save(ProfileHelper.createMockProfile(user));
            profileList.add(profile);

            workoutExerciseList.add(new ArrayList<>());

            Tag tag1 = tagRepository.save(TagHelper.createMockTag());
            Tag tag2 = tagRepository.save(TagHelper.createMockTag());
            tagSetList.add(Set.of(tag1, tag2));
        }

        List<Workout> originalWorkoutList = new ArrayList<>();
        try {
            originalWorkoutList = WorkoutHelper.createMockWorkoutList(profileList, workoutExerciseList, tagSetList);
        } catch (Exception e) {
            Assertions.fail("An error occurred while creating sample data.");
        }

        filterWorkouts_ByName(originalWorkoutList);
        filterWorkouts_ByTagNameList(originalWorkoutList);
        filterWorkouts_ByAuthorId(originalWorkoutList);
        filterWorkouts_ByIsTemplate(originalWorkoutList);
    }

    private void filterWorkouts_ByAuthorId(List<Workout> originalWorkoutList) throws Exception {
        Workout expectedWorkout = originalWorkoutList.get(1);

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), expectedWorkout.getAuthor().getId(), null, null, false);
        PageResponse<WorkoutResponse> response = filter(request);

        WorkoutHelper.assertFilter(response, expectedWorkout);
    }

    private void filterWorkouts_ByTagNameList(List<Workout> originalWorkoutList) throws Exception {
        Workout expectedWorkout = originalWorkoutList.get(2);

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, expectedWorkout.getTagSet().stream().map(Tag::getId).collect(Collectors.toList()), null, false);
        PageResponse<WorkoutResponse> response = filter(request);

        WorkoutHelper.assertFilter(response, expectedWorkout);
    }

    private void filterWorkouts_ByName(List<Workout> originalWorkoutList) throws Exception {
        Workout expectedWorkout = originalWorkoutList.get(0);

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, null, expectedWorkout.getName().substring(0, 5), false);
        PageResponse<WorkoutResponse> response = filter(request);

        WorkoutHelper.assertFilter(response, expectedWorkout);
    }

    private void filterWorkouts_ByIsTemplate(List<Workout> originalWorkoutList) throws Exception {
        Workout expectedWorkout = originalWorkoutList.get(3);

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, null, null, true);
        PageResponse<WorkoutResponse> response = filter(request);

        WorkoutHelper.assertFilter(response, expectedWorkout);
    }

    private PageResponse<WorkoutResponse> filter(WorkoutFilterRequest request) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post("/workouts/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutById() throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));

        MvcResult mvcResult = mockMvc.perform(
                        get("/workouts/" + workout.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WorkoutHelper.assertWorkoutResponse_get(workout, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createWorkout() throws Exception {
        WorkoutCreateRequest request = WorkoutHelper.createCreateRequest();

        MvcResult mvcResult = mockMvc.perform(
                        post("/workouts")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WorkoutHelper.assertWorkoutResponse_create(request, mockProfile, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWorkout() throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WorkoutUpdateRequest request = WorkoutHelper.createUpdateRequest(mockProfile.getId());

        MvcResult mvcResult = mockMvc.perform(
                        put("/workouts/" + workout.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WorkoutHelper.assertWorkoutResponse_update(request, mockProfile, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWorkout() throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));

        MvcResult mvcResult = mockMvc.perform(
                        delete("/workouts/" + workout.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedWorkoutId = Long.parseLong(jsonResponse);

        boolean exists = workoutRepository.findById(deletedWorkoutId).isPresent();

        WorkoutHelper.assertDelete(exists, workout, deletedWorkoutId);
    }
}