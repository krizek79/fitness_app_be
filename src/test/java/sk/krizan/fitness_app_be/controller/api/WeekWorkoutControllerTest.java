package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WeekHelper;
import sk.krizan.fitness_app_be.helper.WeekWorkoutHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WeekRepository;
import sk.krizan.fitness_app_be.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class WeekWorkoutControllerTest {

    @Autowired
    private WeekWorkoutRepository weekWorkoutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private WeekRepository weekRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private Cycle mockCycle;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));

        mockProfile = profileRepository.save(ProfileHelper.createMockProfile(user));

        mockCycle = cycleRepository.save(CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterWeekWorkouts() throws Exception {
        Week week1 = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Workout workout1 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WeekWorkout weekWorkout1 = weekWorkoutRepository.save(WeekWorkoutHelper.createMockWeekWorkout(week1, workout1, 1));

        Week week2 = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 2));
        Workout workout2 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WeekWorkout weekWorkout2 = weekWorkoutRepository.save(WeekWorkoutHelper.createMockWeekWorkout(week2, workout2, 2));

        List<WeekWorkout> originalList = new ArrayList<>(List.of(weekWorkout1, weekWorkout2));
        List<WeekWorkout> expectedList = new ArrayList<>(List.of(originalList.get(0)));

        WeekWorkoutFilterRequest request = WeekWorkoutHelper.createFilterRequest(0, originalList.size(), WeekWorkout.Fields.id, Sort.Direction.DESC.name(), expectedList.get(0).getWeek().getId());

        MvcResult mvcResult = mockMvc.perform(
                        post("/week-workouts/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<WeekWorkoutResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekWorkoutHelper.assertFilter(expectedList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeekWorkoutById() throws Exception {
        Week week = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WeekWorkout weekWorkout = weekWorkoutRepository.save(WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1));

        MvcResult mvcResult = mockMvc.perform(
                        get("/week-workouts/" + weekWorkout.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekWorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekWorkoutHelper.assertWeekWorkoutResponse_get(weekWorkout, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createWeekWorkout() throws Exception {
        Week week = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));

        WeekWorkoutCreateRequest request = WeekWorkoutHelper.createCreateRequest(week.getId(), workout.getId(), 3);

        MvcResult mvcResult = mockMvc.perform(
                        post("/week-workouts")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekWorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekWorkout createdWeekWorkout = weekWorkoutRepository.findById(response.id()).orElseThrow();
        WeekWorkoutHelper.assertWeekWorkout_create(request, response, createdWeekWorkout);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWeekWorkout() throws Exception {
        Week week = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WeekWorkout weekWorkout = weekWorkoutRepository.save(WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1));

        WeekWorkoutUpdateRequest request = WeekWorkoutHelper.createUpdateRequest(2);

        MvcResult mvcResult = mockMvc.perform(
                        put("/week-workouts/" + weekWorkout.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekWorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        weekWorkout = weekWorkoutRepository.findById(weekWorkout.getId()).orElseThrow();

        WeekWorkoutHelper.assertWeekWorkout_update(request, response, weekWorkout);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWeekWorkout() throws Exception {
        Week week = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WeekWorkout weekWorkout = weekWorkoutRepository.save(WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1));

        MvcResult mvcResult = mockMvc.perform(
                        delete("/week-workouts/" + workout.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedWeekWorkoutId = Long.parseLong(jsonResponse);

        boolean exists = weekWorkoutRepository.existsById(deletedWeekWorkoutId);

        WeekWorkoutHelper.assertDelete(exists, weekWorkout, deletedWeekWorkoutId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void triggerCompleted() throws Exception {
        Week week = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WeekWorkout weekWorkout = weekWorkoutRepository.save(WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1));

        Boolean originalState = weekWorkout.getCompleted();

        MvcResult mvcResult = mockMvc.perform(
                        patch("/week-workouts/" + week.getId() + "/trigger-completed")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekWorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        WeekWorkoutHelper.assertTriggerCompleted(originalState, response);
    }
}