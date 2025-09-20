package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

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

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterProfiles() throws Exception {
        User user1 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        User user2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        User user3 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));

        Profile profile1 = ProfileHelper.createMockProfile(user1);
        Profile profile2 = ProfileHelper.createMockProfile(user2);
        Profile profile3 = ProfileHelper.createMockProfile(user3);
        List<Profile> originalList = profileRepository.saveAll(List.of(profile1, profile2, profile3));

        List<Profile> expectedList = new ArrayList<>(List.of(profile2));

        ProfileFilterRequest request = ProfileHelper.createFilterRequest(0, originalList.size(), Profile.Fields.id, Sort.Direction.ASC.name(), expectedList.get(0).getName().substring(0, 5));

        MvcResult mvcResult = mockMvc.perform(
                        post("/profiles/filter")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        PageResponse<ProfileResponse> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        ProfileHelper.assertFilter(expectedList, request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProfileById() throws Exception {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile = profileRepository.save(ProfileHelper.createMockProfile(user));

        MvcResult mvcResult = mockMvc.perform(
                        get("/profiles/" + profile.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ProfileResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        ProfileHelper.assertGet(profile, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProfile() throws Exception {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile = profileRepository.save(ProfileHelper.createMockProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(
                        delete("/profiles/" + profile.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Long deletedProfileId = Long.parseLong(jsonResponse);

        profile = profileRepository.findById(deletedProfileId).orElseThrow();

        ProfileHelper.assertDelete(profile, deletedProfileId);
    }
}