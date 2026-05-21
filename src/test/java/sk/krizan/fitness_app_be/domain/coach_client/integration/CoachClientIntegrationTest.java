package sk.krizan.fitness_app_be.domain.coach_client.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;
import sk.krizan.fitness_app_be.domain.coach_client.helper.CoachClientHelper;
import sk.krizan.fitness_app_be.domain.coach_client.repository.CoachClientRepository;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.domain.coach_client.rest.dto.response.CoachClientResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

class CoachClientIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CoachClientRepository coachClientRepository;

    @MockBean
    private UserService userService;

    private User mockUser1;

    private static final String BASE_URL = "/coach-clients";

    @BeforeEach
    void setUp() {
        mockUser1 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachClients_asCoach() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        filterCoachClientsTest(mockUser2, mockUser1, true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachClients_asClient() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        filterCoachClientsTest(mockUser2, mockUser2, false);
    }

    private void filterCoachClientsTest(User mockUser2, User currentUser, boolean filterByCoach) throws Exception {
        when(userService.getCurrentUser()).thenReturn(currentUser);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        List<CoachClient> originalList = new ArrayList<>(List.of(
                CoachClientHelper.createCoachClient(profile1, profile2),
                CoachClientHelper.createCoachClient(profile2, profile1)));

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

        PageResponse<CoachClientResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                CoachClient::getId,
                CoachClientResponse::id,
                CoachClientHelper::assertCoachClientResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCoachClientById() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachClient coachClient = coachClientRepository.save(CoachClientHelper.createCoachClient(profile1, profile2));

        CoachClientResponse response = performGet(
                BASE_URL + "/" + coachClient.getId()
                , new TypeReference<>() {
                },
                HttpStatus.OK);

        CoachClientHelper.assertCoachClientResponse(coachClient, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCoachClient() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachClientCreateRequest request = CoachClientHelper.createCreateRequest(profile1.getId(), profile2.getId());

        CoachClientResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        CoachClientHelper.assertCreate(request, response, profile1, profile2);
    }

}