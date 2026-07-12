package sk.krizan.fitness_app_be.domain.coaching_contract.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractAction;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContractStatus;
import sk.krizan.fitness_app_be.domain.coaching_contract.helper.CoachingContractHelper;
import sk.krizan.fitness_app_be.domain.coaching_contract.repository.CoachingContractRepository;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterConnectionsRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractStatusUpdateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class CoachingContractIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CoachingContractRepository coachingContractRepository;

    @MockBean
    private UserService userService;

    private User mockUser1;

    private static final String BASE_URL = "/coaching-contracts";

    @BeforeEach
    void setUp() {
        mockUser1 = userRepository.save(UserHelper.createUser());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachingContracts_asCoach() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        filterCoachingContractsTest(mockUser2, mockUser1, true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachingContracts_asClient() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        filterCoachingContractsTest(mockUser2, mockUser2, false);
    }

    private void filterCoachingContractsTest(User mockUser2, User currentUser, boolean filterByCoach) throws Exception {
        when(userService.getOrCreateCurrentUser()).thenReturn(currentUser);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        List<CoachingContract> originalList = new ArrayList<>(List.of(
                CoachingContractHelper.createCoachingContract(profile1, profile2),
                CoachingContractHelper.createCoachingContract(profile2, profile1)));

        originalList = coachingContractRepository.saveAll(originalList);

        CoachingContract expected = originalList.get(0);
        List<CoachingContract> expectedList = new ArrayList<>(List.of(expected));

        Long coachId = filterByCoach ? profile1.getId() : null;
        Long clientId = filterByCoach ? null : profile2.getId();

        CoachingContractFilterRequest request = CoachingContractHelper.createFilterRequest(
                0,
                originalList.size(),
                CoachingContract.Fields.id,
                Sort.Direction.ASC.name(),
                coachId,
                clientId
        );

        PageResponse<CoachingContractResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                CoachingContract::getId,
                CoachingContractResponse::id,
                CoachingContractHelper::assertCoachingContractResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCoachingContractById() throws Exception {
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createUser());
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContract coachingContract = coachingContractRepository.save(CoachingContractHelper.createCoachingContract(profile1, profile2));

        CoachingContractResponse response = performGet(
                BASE_URL + "/" + coachingContract.getId()
                , new TypeReference<>() {
                },
                HttpStatus.OK);

        CoachingContractHelper.assertCoachingContractResponse(coachingContract, response);
    }

    @Test
    @WithMockUser
    void filterCoachingContracts_nonMemberSeesNothing() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser3);
        when(userService.isUserAdmin(mockUser3)).thenReturn(false);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        profileRepository.save(ProfileHelper.createProfile(mockUser3));

        coachingContractRepository.saveAll(List.of(
                CoachingContractHelper.createCoachingContract(profile1, profile2),
                CoachingContractHelper.createCoachingContract(profile2, profile1)
        ));

        CoachingContractFilterRequest request = CoachingContractHelper.createFilterRequest(
                0, 10, CoachingContract.Fields.id, Sort.Direction.ASC.name(), null, null);

        PageResponse<CoachingContractResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertEquals(0, response.getTotalElements());
        Assertions.assertTrue(response.getResults().isEmpty());
    }

    @Test
    @WithMockUser
    void filterClients_ownProfileOnPageZero() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client1 = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        Profile client2 = profileRepository.save(ProfileHelper.createProfile(mockUser3));

        coachingContractRepository.saveAll(List.of(
                CoachingContractHelper.createCoachingContract(coach, client1),
                CoachingContractHelper.createCoachingContract(coach, client2)
        ));

        CoachingContractFilterConnectionsRequest request = CoachingContractHelper.createFilterConnectionsRequest(
                0, 10, CoachingContract.Fields.id, Sort.Direction.ASC.name(), null);

        PageResponse<ProfileSimpleResponse> response = performPost(
                BASE_URL + "/clients",
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertEquals(3, response.getTotalElements());
        Assertions.assertEquals(3, response.getResults().size());
        ProfileHelper.assertProfileSimpleResponse(coach, response.getResults().get(0));
    }

    @Test
    @WithMockUser
    void filterClients_inactiveContractsExcluded() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContract inactiveContract = CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.TERMINATED);
        coachingContractRepository.save(inactiveContract);

        CoachingContractFilterConnectionsRequest request = CoachingContractHelper.createFilterConnectionsRequest(
                0, 10, CoachingContract.Fields.id, Sort.Direction.ASC.name(), null);

        PageResponse<ProfileSimpleResponse> response = performPost(
                BASE_URL + "/clients",
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertEquals(1, response.getTotalElements());
        Assertions.assertEquals(1, response.getResults().size());
        ProfileHelper.assertProfileSimpleResponse(coach, response.getResults().get(0));
    }

    @Test
    @WithMockUser
    void filterClients_ownProfileNotOnSubsequentPages() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client1 = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        Profile client2 = profileRepository.save(ProfileHelper.createProfile(mockUser3));

        coachingContractRepository.saveAll(List.of(
                CoachingContractHelper.createCoachingContract(coach, client1),
                CoachingContractHelper.createCoachingContract(coach, client2)
        ));

        // size=1, page=1 â†’ DB page 1 returns client2; own profile must not appear
        CoachingContractFilterConnectionsRequest request = CoachingContractHelper.createFilterConnectionsRequest(
                1, 1, CoachingContract.Fields.id, Sort.Direction.ASC.name(), null);

        PageResponse<ProfileSimpleResponse> response = performPost(
                BASE_URL + "/clients",
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertEquals(1, response.getResults().size());
        Assertions.assertNotEquals(coach.getId(), response.getResults().get(0).id());
    }

    @Test
    @WithMockUser
    void filterCoaches_ownProfileOnPageZero() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile coach1 = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        Profile coach2 = profileRepository.save(ProfileHelper.createProfile(mockUser3));

        coachingContractRepository.saveAll(List.of(
                CoachingContractHelper.createCoachingContract(coach1, client),
                CoachingContractHelper.createCoachingContract(coach2, client)
        ));

        CoachingContractFilterConnectionsRequest request = CoachingContractHelper.createFilterConnectionsRequest(
                0, 10, CoachingContract.Fields.id, Sort.Direction.ASC.name(), null);

        PageResponse<ProfileSimpleResponse> response = performPost(
                BASE_URL + "/coaches",
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertEquals(3, response.getTotalElements());
        Assertions.assertEquals(3, response.getResults().size());
        ProfileHelper.assertProfileSimpleResponse(client, response.getResults().get(0));
    }

    @Test
    @WithMockUser
    void filterCoaches_inactiveContractsExcluded() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContract inactiveContract = CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.TERMINATED);
        coachingContractRepository.save(inactiveContract);

        CoachingContractFilterConnectionsRequest request = CoachingContractHelper.createFilterConnectionsRequest(
                0, 10, CoachingContract.Fields.id, Sort.Direction.ASC.name(), null);

        PageResponse<ProfileSimpleResponse> response = performPost(
                BASE_URL + "/coaches",
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertEquals(1, response.getTotalElements());
        Assertions.assertEquals(1, response.getResults().size());
        ProfileHelper.assertProfileSimpleResponse(client, response.getResults().get(0));
    }

    @Test
    @WithMockUser
    void filterCoaches_ownProfileNotOnSubsequentPages() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile coach1 = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        Profile coach2 = profileRepository.save(ProfileHelper.createProfile(mockUser3));

        coachingContractRepository.saveAll(List.of(
                CoachingContractHelper.createCoachingContract(coach1, client),
                CoachingContractHelper.createCoachingContract(coach2, client)
        ));

        // size=1, page=1 → DB page 1 returns coach2; own profile must not appear
        CoachingContractFilterConnectionsRequest request = CoachingContractHelper.createFilterConnectionsRequest(
                1, 1, CoachingContract.Fields.id, Sort.Direction.ASC.name(), null);

        PageResponse<ProfileSimpleResponse> response = performPost(
                BASE_URL + "/coaches",
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertEquals(1, response.getResults().size());
        Assertions.assertNotEquals(client.getId(), response.getResults().get(0).id());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCoachingContract() throws Exception {
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createUser());
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContractCreateRequest request = CoachingContractHelper.createCreateRequest(profile1.getId(), profile2.getId());

        CoachingContractResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        CoachingContractHelper.assertCreate(response, profile1, profile2);
    }

    @Test
    @WithMockUser
    void createCoachingContract_notAsCoach_returns403() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser3);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        profileRepository.save(ProfileHelper.createProfile(mockUser3));

        CoachingContractCreateRequest request = CoachingContractHelper.createCreateRequest(profile1.getId(), profile2.getId());

        performPost(
                BASE_URL,
                request,
                new TypeReference<ProblemDetails>() {},
                HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser
    void createCoachingContract_duplicatePendingOrActive_returns409() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.PENDING));

        CoachingContractCreateRequest request = CoachingContractHelper.createCreateRequest(coach.getId(), client.getId());

        performPost(
                BASE_URL,
                request,
                new TypeReference<ProblemDetails>() {},
                HttpStatus.CONFLICT);
    }

    @Test
    @WithMockUser
    void getCoachingContractById_nonParty_returns403() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        profileRepository.save(ProfileHelper.createProfile(mockUser3));

        CoachingContract coachingContract = coachingContractRepository.save(
                CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.ACTIVE));

        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser3);

        performGet(
                BASE_URL + "/" + coachingContract.getId(),
                new TypeReference<ProblemDetails>() {},
                HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser
    void updateStatus_accept_pendingToActive_byClient() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContract coachingContract = coachingContractRepository.save(
                CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.PENDING));

        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser2);

        CoachingContractResponse response = performPatch(
                BASE_URL + "/" + coachingContract.getId() + "/status",
                CoachingContractStatusUpdateRequest.builder().action(CoachingContractAction.ACCEPT).build(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertEquals(CoachingContractStatus.ACTIVE, response.status());
    }

    @Test
    @WithMockUser
    void updateStatus_terminate_activeToTerminated_byCoach() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContract coachingContract = coachingContractRepository.save(
                CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.ACTIVE));

        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        CoachingContractResponse response = performPatch(
                BASE_URL + "/" + coachingContract.getId() + "/status",
                CoachingContractStatusUpdateRequest.builder().action(CoachingContractAction.TERMINATE).build(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertEquals(CoachingContractStatus.TERMINATED, response.status());
    }

    @Test
    @WithMockUser
    void updateStatus_wrongCallerRole_returns409() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContract coachingContract = coachingContractRepository.save(
                CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.PENDING));

        // Only the client may ACCEPT a PENDING request - the coach attempting it must be rejected.
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser1);

        performPatch(
                BASE_URL + "/" + coachingContract.getId() + "/status",
                CoachingContractStatusUpdateRequest.builder().action(CoachingContractAction.ACCEPT).build(),
                new TypeReference<ProblemDetails>() {},
                HttpStatus.CONFLICT);
    }

    @Test
    @WithMockUser
    void updateStatus_invalidFromStatus_returns409() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));

        CoachingContract coachingContract = coachingContractRepository.save(
                CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.ACTIVE));

        // ACCEPT is only valid from PENDING - an already-ACTIVE contract must be rejected.
        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser2);

        performPatch(
                BASE_URL + "/" + coachingContract.getId() + "/status",
                CoachingContractStatusUpdateRequest.builder().action(CoachingContractAction.ACCEPT).build(),
                new TypeReference<ProblemDetails>() {},
                HttpStatus.CONFLICT);
    }

    @Test
    @WithMockUser
    void updateStatus_nonParty_returns403() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser());
        User mockUser3 = userRepository.save(UserHelper.createUser());

        Profile coach = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        Profile client = profileRepository.save(ProfileHelper.createProfile(mockUser2));
        profileRepository.save(ProfileHelper.createProfile(mockUser3));

        CoachingContract coachingContract = coachingContractRepository.save(
                CoachingContractHelper.createCoachingContract(coach, client, CoachingContractStatus.PENDING));

        when(userService.getOrCreateCurrentUser()).thenReturn(mockUser3);

        performPatch(
                BASE_URL + "/" + coachingContract.getId() + "/status",
                CoachingContractStatusUpdateRequest.builder().action(CoachingContractAction.ACCEPT).build(),
                new TypeReference<ProblemDetails>() {},
                HttpStatus.FORBIDDEN);
    }

}