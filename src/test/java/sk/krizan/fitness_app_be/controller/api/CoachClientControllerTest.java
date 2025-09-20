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
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.CoachClientHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.CoachClient;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CoachClientRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CoachClientControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CoachClientRepository coachClientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User mockUser1;

    @BeforeEach
    void setUp() {
        mockUser1 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachClients_asCoach() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        filterCoachClientsTest(mockUser2, mockUser1, true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachClients_asClient() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        filterCoachClientsTest(mockUser2, mockUser2, false);
    }

    private void filterCoachClientsTest(User mockUser2, User currentUser, boolean filterByCoach) throws Exception {
        when(userService.getCurrentUser()).thenReturn(currentUser);

        Profile profile1 = profileRepository.save(ProfileHelper.createMockProfile(mockUser1));
        Profile profile2 = profileRepository.save(ProfileHelper.createMockProfile(mockUser2));

        List<CoachClient> originalList = new ArrayList<>(List.of(
                CoachClientHelper.createMockCoachClient(profile1, profile2),
                CoachClientHelper.createMockCoachClient(profile2, profile1)));

        originalList = coachClientRepository.saveAll(originalList);

        CoachClient expected = originalList.get(0);
        List<CoachClient> expectedList = new ArrayList<>(List.of(expected));

        Long coachId = filterByCoach ? profile1.getId() : null;
        Long clientId = filterByCoach ? null : profile2.getId();

        CoachClientFilterRequest request = CoachClientHelper.createFilterRequest(
                0,
                originalList.size(),
                CoachClient.Fields.id,
                Sort.Direction.ASC.name(),
                coachId,
                clientId
        );

        MvcResult mvcResult = mockMvc.perform(
                        post("/coach-clients/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<CoachClientResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        CoachClientHelper.assertFilter(expectedList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCoachClientById() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createMockProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile2 = profileRepository.save(ProfileHelper.createMockProfile(mockUser2));

        CoachClient coachClient = coachClientRepository.save(CoachClientHelper.createMockCoachClient(profile1, profile2));

        MvcResult mvcResult = mockMvc.perform(
                        get("/coach-clients/" + coachClient.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CoachClientResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        CoachClientHelper.assertCoachClientResponse(response, coachClient);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCoachClient() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createMockProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile2 = profileRepository.save(ProfileHelper.createMockProfile(mockUser2));

        CoachClientCreateRequest request = CoachClientHelper.createCreateRequest(profile1.getId(), profile2.getId());

        MvcResult mvcResult = mockMvc.perform(
                        post("/coach-clients")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CoachClientResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        CoachClientHelper.assertCreate(request, response, profile1, profile2);
    }
}