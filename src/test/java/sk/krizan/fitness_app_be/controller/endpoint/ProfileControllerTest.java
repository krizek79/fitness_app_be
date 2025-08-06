package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.endpoint.api.ProfileController;
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@SpringBootTest
class ProfileControllerTest {

    @Autowired
    private ProfileController profileController;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User mockUser = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));
        SecurityHelper.setAuthentication(mockUser);
    }

    @Test
    void filterProfiles() {
        User user1 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        User user2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        User user3 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));

        Profile profile1 = ProfileHelper.createMockProfile(user1);
        Profile profile2 = ProfileHelper.createMockProfile(user2);
        Profile profile3 = ProfileHelper.createMockProfile(user3);
        List<Profile> originalList = profileRepository.saveAll(List.of(profile1, profile2, profile3));

        List<Profile> expectedList = new ArrayList<>(List.of(profile2));

        ProfileFilterRequest request = ProfileHelper.createFilterRequest(0, originalList.size(), Profile.Fields.id, Sort.Direction.ASC.name(), expectedList.get(0).getName().substring(0, 5));
        PageResponse<ProfileResponse> response = profileController.filterProfiles(request);

        ProfileHelper.assertFilter(expectedList, request, response);
    }

    @Test
    void getProfileById() {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile = profileRepository.save(ProfileHelper.createMockProfile(user));

        ProfileResponse response = profileController.getProfileById(profile.getId());

        ProfileHelper.assertGet(profile, response);
    }

    @Test
    void deleteProfile() {
        User user = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile = profileRepository.save(ProfileHelper.createMockProfile(user));

        Long deletedProfileId = profileController.deleteProfile(profile.getId());

        profile = profileRepository.findById(deletedProfileId).orElseThrow();

        ProfileHelper.assertDelete(profile, deletedProfileId);
    }
}