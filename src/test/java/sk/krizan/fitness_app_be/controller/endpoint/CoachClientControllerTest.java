package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.endpoint.api.CoachClientController;
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.CoachClientHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.CoachClient;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CoachClientRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@SpringBootTest
class CoachClientControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CoachClientController coachClientController;

    @Autowired
    private CoachClientRepository coachClientRepository;

    private User mockUser1;

    @BeforeEach
    void setUp() {
        mockUser1 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        SecurityHelper.setAuthentication(mockUser1);
    }

    @Test
    void filterCoachClients_asCoach() {
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        filterCoachClientsTest(mockUser2, mockUser1, true);
    }

    @Test
    void filterCoachClients_asClient() {
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        filterCoachClientsTest(mockUser2, mockUser2, false);
    }

    private void filterCoachClientsTest(User mockUser2, User currentUser, boolean filterByCoach) {
        SecurityHelper.setAuthentication(currentUser);

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

        PageResponse<CoachClientResponse> response = coachClientController.filterCoachClients(request);
        CoachClientHelper.assertFilter(expectedList, request, response);
    }


    @Test
    void getCoachClientById() {
        SecurityHelper.setAuthentication(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createMockProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile2 = profileRepository.save(ProfileHelper.createMockProfile(mockUser2));

        CoachClient coachClient = coachClientRepository.save(CoachClientHelper.createMockCoachClient(profile1, profile2));

        CoachClientResponse response = coachClientController.getCoachClientById(coachClient.getId());

        CoachClientHelper.assertCoachClientResponse(response, coachClient);
    }

    @Test
    void createCoachClient() {
        SecurityHelper.setAuthentication(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createMockProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createMockUser(Set.of(Role.USER)));
        Profile profile2 = profileRepository.save(ProfileHelper.createMockProfile(mockUser2));

        CoachClientCreateRequest request = CoachClientHelper.createCreateRequest(profile1.getId(), profile2.getId());
        CoachClientResponse response = coachClientController.createCoachClient(request);

        CoachClientHelper.assertCreate(request, response, profile1, profile2);
    }
}