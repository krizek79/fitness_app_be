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
import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.GoalHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Goal;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.GoalRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class GoalControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private GoalRepository goalRepository;

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
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));

        mockProfile = profileRepository.save(ProfileHelper.createMockProfile(mockUser));

        mockCycle = cycleRepository.save(CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterGoals() throws Exception {
        Cycle mockCycle2 = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        mockCycle2 = cycleRepository.save(mockCycle2);
        List<Goal> originalList = GoalHelper.createMockGoalList(new ArrayList<>(List.of(mockCycle, mockCycle2)));
        List<Goal> savedGoalList = goalRepository.saveAll(originalList);
        List<Goal> expectedList = savedGoalList.stream().filter(goal -> goal.getCycle().getId().equals(mockCycle.getId())).collect(Collectors.toList());

        GoalFilterRequest request = GoalHelper.createFilterRequest(0, originalList.size(), Goal.Fields.id, Sort.Direction.DESC.name(), mockCycle.getId());

        MvcResult mvcResult = mockMvc.perform(
                        post("/goals/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<GoalResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        GoalHelper.assertFilter(expectedList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGoalById() throws Exception {
        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);

        MvcResult mvcResult = mockMvc.perform(
                        get("/goals/" + goal.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        GoalResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        GoalHelper.assertGoalResponse_get(goal, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGoal() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser);

        GoalCreateRequest request = GoalHelper.createCreateRequest(mockCycle.getId());

        MvcResult mvcResult = mockMvc.perform(
                        post("/goals")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        GoalResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        GoalHelper.assertGoalResponse_create(request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGoal() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);
        GoalUpdateRequest request = GoalHelper.createUpdateRequest();

        MvcResult mvcResult = mockMvc.perform(
                        put("/goals/" + goal.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        GoalResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        GoalHelper.assertGoalResponse_update(request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGoal() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);

        MvcResult mvcResult = mockMvc.perform(
                        delete("/goals/" + goal.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        long deletedGoalId = Long.parseLong(jsonResponse);

        boolean exists = goalRepository.existsById(deletedGoalId);

        GoalHelper.assertDelete(exists, goal, deletedGoalId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void triggerAchieved() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);
        Boolean originalState = goal.getAchieved();

        MvcResult mvcResult = mockMvc.perform(
                        patch("/goals/" + goal.getId() + "/trigger-achieved")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        GoalResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        GoalHelper.assertTriggerAchieved(originalState, response);
    }
}