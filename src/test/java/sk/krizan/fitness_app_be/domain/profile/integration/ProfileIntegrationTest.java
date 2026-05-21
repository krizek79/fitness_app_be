package sk.krizan.fitness_app_be.domain.profile.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileResponse;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

class ProfileIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private static final String BASE_URL = "/profiles";

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterProfiles() throws Exception {
        User user1 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        User user2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        User user3 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));

        Profile profile1 = ProfileHelper.createProfile(user1);
        Profile profile2 = ProfileHelper.createProfile(user2);
        Profile profile3 = ProfileHelper.createProfile(user3);
        List<Profile> originalList = profileRepository.saveAll(List.of(profile1, profile2, profile3));

        List<Profile> expectedList = new ArrayList<>(List.of(profile2));

        ProfileFilterRequest request = ProfileHelper.createFilterRequest(0, originalList.size(), Profile.Fields.id, Sort.Direction.ASC.name(), expectedList.get(0).getName().substring(0, 5));

        PageResponse<ProfileResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Profile::getId,
                ProfileResponse::id,
                ProfileHelper::assertProfileResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProfileById() throws Exception {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile profile = profileRepository.save(ProfileHelper.createProfile(user));

        ProfileResponse response = performGet(
                BASE_URL + "/" + profile.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        ProfileHelper.assertProfileResponse(profile, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProfile() throws Exception {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile profile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);

        performDeleteNoResponse(BASE_URL + "/" + profile.getId(), HttpStatus.NO_CONTENT);

        profile = profileRepository.findById(profile.getId()).orElseThrow();

        Assertions.assertTrue(profile.getDeleted());
    }

}