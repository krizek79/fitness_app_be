package sk.krizan.fitness_app_be.domain.coaching_contract.integration;

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
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.coaching_contract.helper.CoachingContractHelper;
import sk.krizan.fitness_app_be.domain.coaching_contract.repository.CoachingContractRepository;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
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
        mockUser1 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachingContracts_asCoach() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        filterCoachingContractsTest(mockUser2, mockUser1, true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCoachingContracts_asClient() throws Exception {
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        filterCoachingContractsTest(mockUser2, mockUser2, false);
    }

    private void filterCoachingContractsTest(User mockUser2, User currentUser, boolean filterByCoach) throws Exception {
        when(userService.getCurrentUser()).thenReturn(currentUser);

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
        when(userService.getCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
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
    @WithMockUser(roles = "ADMIN")
    void createCoachingContract() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser1);

        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(mockUser1));
        User mockUser2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
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

}