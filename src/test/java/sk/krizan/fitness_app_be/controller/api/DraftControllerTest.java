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
import sk.krizan.fitness_app_be.controller.request.draft.DraftCreateRequest;
import sk.krizan.fitness_app_be.controller.request.draft.DraftFilterRequest;
import sk.krizan.fitness_app_be.controller.request.draft.DraftUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.DraftResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.DraftHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Draft;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.DraftEntityType;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.DraftRepository;
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
public class DraftControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DraftRepository draftRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    private final static String BASE_URL = "/drafts";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createMockProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterDrafts() throws Exception {
        User user1 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile1 = profileRepository.save(ProfileHelper.createMockProfile(user1));

        List<Draft> originalList = DraftHelper.createMockDraftListForFilter(profile1, mockProfile);
        originalList = draftRepository.saveAll(originalList);

        List<Draft> expectedList = new ArrayList<>(List.of(originalList.get(originalList.size() - 2)));

        DraftFilterRequest request = DraftHelper.createFilterRequest(0, expectedList.size(), Draft.Fields.id, Sort.Direction.ASC.name(), expectedList.get(0).getEntityType(), expectedList.get(0).getTitle());

        MvcResult mvcResult = mockMvc.perform(
                        post(BASE_URL + "/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        PageResponse<DraftResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        DraftHelper.assertFilter(expectedList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDraftById() throws Exception {
        Draft draft = draftRepository.save(DraftHelper.createMockDraft(mockProfile, DraftEntityType.CYCLE, "Cycle Draft", DraftHelper.createMockContent("Cycle Draft", "{\"someAttribute\":\"someValue\"}")));

        MvcResult mvcResult = mockMvc.perform(
                        get(BASE_URL + "/" + draft.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        DraftResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        DraftHelper.assertResponse(draft, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDraft() throws Exception {
        DraftCreateRequest request = DraftHelper.createCreateRequest(
                DraftEntityType.CYCLE,
                DraftHelper.createMockContent("Cycle A", "{\"someAttribute\":\"someValue1\"}")
        );
        String expectedTitle = request.content().get("title").toString();

        MvcResult mvcResult = mockMvc.perform(
                        post(BASE_URL)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        DraftResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        DraftHelper.assertCreateRequest(request, response, mockProfile, expectedTitle);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDraft() throws Exception {
        Draft draft = draftRepository.save(DraftHelper.createMockDraft(mockProfile, DraftEntityType.CYCLE, "Cycle To Update", DraftHelper.createMockContent("Cycle To Update", "{\"someAttribute\":\"someValueToUpdate\"}")));

        DraftUpdateRequest request = DraftHelper.createUpdateRequest(DraftHelper.createMockContent("Updated Cycle", "{\"someAttribute\":\"updatedValue\"}"));
        String expectedTitle = request.content().get("title").toString();

        MvcResult mvcResult = mockMvc.perform(
                        put(BASE_URL + "/" + draft.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        DraftResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        Draft updatedDraft = draftRepository.findById(draft.getId()).orElseThrow();

        DraftHelper.assertUpdateRequest(request, response, updatedDraft, expectedTitle);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDraft() throws Exception {
        Draft draft = draftRepository.save(DraftHelper.createMockDraft(mockProfile, DraftEntityType.CYCLE, "Cycle To Delete", DraftHelper.createMockContent("Cycle To Delete", "{\"someAttribute\":\"someValueToDelete\"}")));

        mockMvc.perform(
                        delete(BASE_URL + "/" + draft.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        boolean exists = draftRepository.existsById(draft.getId());

        Assertions.assertFalse(exists);
    }
}
