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
import sk.krizan.fitness_app_be.controller.endpoint.api.CycleController;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.CoachClientHelper;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CoachClientRepository;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CycleControllerTest {

    @Autowired
    private CoachClientRepository coachClientRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleController cycleController;

    @Autowired
    private CycleRepository cycleRepository;

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
    void filterCycles() throws Exception {
        User user1 = UserHelper.createMockUser(Set.of(Role.USER));
        user1 = userRepository.save(user1);
        Profile profile1 = ProfileHelper.createMockProfile(user1);
        profile1 = profileRepository.save(profile1);
        User user2 = UserHelper.createMockUser(Set.of(Role.USER));
        user2 = userRepository.save(user2);
        Profile profile2 = ProfileHelper.createMockProfile(user2);
        profile2 = profileRepository.save(profile2);

        List<Cycle> originalList = CycleHelper.createMockCycleListForFilter(profile1, profile2);
        originalList = cycleRepository.saveAll(originalList);

        filterCycles_byAuthorId(originalList, profile1.getId());
        filterCycles_byTraineeId(originalList, profile2.getId());
        filterCycles_byName(originalList);
        filterCycles_byLevelKey(originalList);
    }

    private void filterCycles_byAuthorId(List<Cycle> originalList, Long profileId) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(0), originalList.get(1)));
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), profileId, null, null, null);
        PageResponse<CycleResponse> response = filter(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    private void filterCycles_byTraineeId(List<Cycle> originalList, Long profileId) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(1), originalList.get(3)));
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, profileId, null, null);
        PageResponse<CycleResponse> response = filter(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    private void filterCycles_byName(List<Cycle> originalList) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(2)));
        String name = expectedList.get(0).getName().substring(0, 5);
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, null, name, null);
        PageResponse<CycleResponse> response = filter(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    private void filterCycles_byLevelKey(List<Cycle> originalList) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(3)));
        Level level = expectedList.get(0).getLevel();
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, null, null, level);
        PageResponse<CycleResponse> response = filter(request);
        CycleHelper.assertFilter(expectedList, request, response);
    }

    private PageResponse<CycleResponse> filter(CycleFilterRequest request) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post("/cycles/filter")
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
    void getCycleById() throws Exception {
        Cycle cycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        cycle = cycleRepository.save(cycle);

        MvcResult mvcResult = mockMvc.perform(
                        get("/cycles/" + cycle.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CycleResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        CycleHelper.assertCycleResponse_get(cycle, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCycle() {
        CycleCreateRequest createRequest = CycleHelper.createCreateRequest();
        CycleResponse response = cycleController.createCycle(createRequest);
        CycleHelper.assertCycleResponse_create(createRequest, mockProfile, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCycle() throws Exception {
        Cycle cycle = cycleRepository.save(CycleHelper.createMockCycle(mockProfile, mockProfile, Level.ADVANCED));

        User traineeUser = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile traineeProfile = profileRepository.save(ProfileHelper.createMockProfile(traineeUser));

        coachClientRepository.save(CoachClientHelper.createMockCoachClient(mockProfile, traineeProfile));

        CycleUpdateRequest request = CycleHelper.createUpdateRequest(Level.INTERMEDIATE, traineeProfile.getId());

        MvcResult mvcResult = mockMvc.perform(
                        put("/cycles/" + cycle.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CycleResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        CycleHelper.assertCycleResponse_update(request, mockProfile, traineeProfile, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCycle() throws Exception {
        Cycle cycle = cycleRepository.save(CycleHelper.createMockCycle(mockProfile, mockProfile, Level.INTERMEDIATE));

        MvcResult mvcResult = mockMvc.perform(
                        delete("/cycles/" + cycle.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedCycleId = Long.parseLong(jsonResponse);

        boolean exists = cycleRepository.existsById(deletedCycleId);

        CycleHelper.assertDelete(exists, cycle, deletedCycleId);
    }
}