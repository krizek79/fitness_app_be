package sk.krizan.fitness_app_be.domain.plan.integration;

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
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.coaching_contract.helper.CoachingContractHelper;
import sk.krizan.fitness_app_be.domain.coaching_contract.repository.CoachingContractRepository;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.helper.PlanHelper;
import sk.krizan.fitness_app_be.domain.plan.repository.PlanRepository;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanDetailResponse;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanSimpleResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.helper.WeekHelper;
import sk.krizan.fitness_app_be.domain.week.repository.WeekRepository;
import sk.krizan.fitness_app_be.domain.week.rest.dto.request.WeekInputRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class PlanIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CoachingContractRepository coachingContractRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private WeekRepository weekRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    private static final String BASE_URL = "/plans";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser());
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getOrCreateCurrentUser()).thenReturn(user);
        when(userService.isUserAdmin(user)).thenReturn(true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterPlans() throws Exception {
        User user1 = UserHelper.createUser();
        user1 = userRepository.save(user1);
        Profile profile1 = ProfileHelper.createProfile(user1);
        profile1 = profileRepository.save(profile1);
        User user2 = UserHelper.createUser();
        user2 = userRepository.save(user2);
        Profile profile2 = ProfileHelper.createProfile(user2);
        profile2 = profileRepository.save(profile2);

        List<Plan> originalList = PlanHelper.createMockPlanListForFilter(profile1, profile2);
        originalList = planRepository.saveAll(originalList);

        filterPlans_byAuthorId(originalList, profile1.getId());
        filterPlans_byTraineeId(originalList, profile2.getId());
        filterPlans_byName(originalList);
    }

    private void filterPlans_byAuthorId(List<Plan> originalList, Long profileId) throws Exception {
        List<Plan> expectedList = new ArrayList<>(List.of(originalList.get(0), originalList.get(1)));
        PlanFilterRequest request = PlanHelper.createFilterRequest(0, originalList.size(), Plan.Fields.id, Sort.Direction.DESC.name(), profileId, null, null);
        PageResponse<PlanSimpleResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Plan::getId,
                PlanSimpleResponse::id,
                PlanHelper::assertPlanSimpleResponse);
    }

    private void filterPlans_byTraineeId(List<Plan> originalList, Long profileId) throws Exception {
        List<Plan> expectedList = new ArrayList<>(List.of(originalList.get(1), originalList.get(3)));
        PlanFilterRequest request = PlanHelper.createFilterRequest(0, originalList.size(), Plan.Fields.id, Sort.Direction.DESC.name(), null, profileId, null);
        PageResponse<PlanSimpleResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Plan::getId,
                PlanSimpleResponse::id,
                PlanHelper::assertPlanSimpleResponse);
    }

    private void filterPlans_byName(List<Plan> originalList) throws Exception {
        List<Plan> expectedList = new ArrayList<>(List.of(originalList.get(2)));
        String title = expectedList.get(0).getTitle().substring(0, 5);
        PlanFilterRequest request = PlanHelper.createFilterRequest(0, originalList.size(), Plan.Fields.id, Sort.Direction.DESC.name(), null, null, title);
        PageResponse<PlanSimpleResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Plan::getId,
                PlanSimpleResponse::id,
                PlanHelper::assertPlanSimpleResponse);
    }

    private PageResponse<PlanSimpleResponse> filter(PlanFilterRequest request) throws Exception {
        return performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);
    }

    @Test
    @WithMockUser
    void filterPlans_clientCannotSeeCoachesPlansForOtherTrainees() throws Exception {
        User coachUser = userRepository.save(UserHelper.createUser());
        Profile coachProfile = profileRepository.save(ProfileHelper.createProfile(coachUser));
        User clientUser = userRepository.save(UserHelper.createUser());
        Profile clientProfile = profileRepository.save(ProfileHelper.createProfile(clientUser));
        User otherClientUser = userRepository.save(UserHelper.createUser());
        Profile otherClientProfile = profileRepository.save(ProfileHelper.createProfile(otherClientUser));

        when(userService.getOrCreateCurrentUser()).thenReturn(clientUser);
        when(userService.isUserAdmin(clientUser)).thenReturn(false);

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(coachProfile, clientProfile));

        Plan ownPlan = planRepository.save(PlanHelper.createPlan(coachProfile, clientProfile, new ArrayList<>()));
        planRepository.save(PlanHelper.createPlan(coachProfile, otherClientProfile, new ArrayList<>()));
        planRepository.save(PlanHelper.createPlan(coachProfile, null, new ArrayList<>()));

        PlanFilterRequest request = PlanHelper.createFilterRequest(
                0, 10, Plan.Fields.id, Sort.Direction.ASC.name(), coachProfile.getId(), null, null);

        PageResponse<PlanSimpleResponse> response = filter(request);

        FilterAssertionUtils.assertFilterResults(
                new ArrayList<>(List.of(ownPlan)),
                response,
                Plan::getId,
                PlanSimpleResponse::id,
                PlanHelper::assertPlanSimpleResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPlanById() throws Exception {
        List<Week> weeks = new ArrayList<>();

        Plan plan = PlanHelper.createPlan(mockProfile, mockProfile, weeks);
        plan = planRepository.save(plan);

        PlanDetailResponse response = performGet(
                BASE_URL + "/" + plan.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        PlanHelper.assertPlanDetailResponse(plan, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePlan() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks));

        performDeleteNoResponse(
                BASE_URL + "/" + plan.getId(),
                HttpStatus.NO_CONTENT);

        boolean planExists = planRepository.existsById(plan.getId());
        boolean weekExists = weekRepository.existsById(weeks.get(0).getId());

        PlanHelper.assertDelete(planExists, weekExists);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPlan() throws Exception {
        User trainee = userRepository.save(UserHelper.createUser());
        Profile traineeProfile = profileRepository.save(ProfileHelper.createProfile(trainee));

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, traineeProfile));

        List<WeekInputRequest> weeks = List.of(WeekHelper.createInputRequest(null, 1, "Note"));
        PlanInputRequest request = PlanHelper.createInputRequest(
                traineeProfile.getId(),
                weeks
        );

        PlanDetailResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Plan savedPlan = planRepository.getByIdOrThrow(response.id());
        PlanHelper.assertInputToEntity(savedPlan, request);
        Assertions.assertEquals(traineeProfile.getId(), savedPlan.getTrainee().getId());
        PlanHelper.assertPlanDetailResponse(savedPlan, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePlan() throws Exception {
        User trainee1 = userRepository.save(UserHelper.createUser());
        Profile traineeProfile1 = profileRepository.save(ProfileHelper.createProfile(trainee1));
        User trainee2 = userRepository.save(UserHelper.createUser());
        Profile traineeProfile2 = profileRepository.save(ProfileHelper.createProfile(trainee2));

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, traineeProfile1));
        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, traineeProfile2));

        // Create plan with initial weeks

        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1),
                WeekHelper.createWeek(2)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, traineeProfile1, weeks));

        Long firstWeekId = plan.getWeeks().get(0).getId();

        // Update: keep first week, remove second week, add new week
        List<WeekInputRequest> updatedWeeks = List.of(
                WeekHelper.createInputRequest(firstWeekId, 1, "Week 1 Updated"),
                WeekHelper.createInputRequest(null, 2, "Week 2 New")
        );
        PlanInputRequest request = PlanHelper.createInputRequest(
                traineeProfile2.getId(),
                updatedWeeks
        );

        PlanDetailResponse response = performPut(
                BASE_URL + "/" + plan.getId(),
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Plan updatedPlan = planRepository.getByIdOrThrow(response.id());
        PlanHelper.assertInputToEntity(updatedPlan, request);
        PlanHelper.assertPlanDetailResponse(updatedPlan, response);
    }

}