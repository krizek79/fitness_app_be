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
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanResponse;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.helper.GoalHelper;
import sk.krizan.fitness_app_be.domain.goal.repository.GoalRepository;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
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
import java.util.Set;

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
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    private static final String BASE_URL = "/plans";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterPlans() throws Exception {
        User user1 = UserHelper.createUser(Set.of(Role.USER));
        user1 = userRepository.save(user1);
        Profile profile1 = ProfileHelper.createProfile(user1);
        profile1 = profileRepository.save(profile1);
        User user2 = UserHelper.createUser(Set.of(Role.USER));
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
        PageResponse<PlanResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Plan::getId,
                PlanResponse::id,
                PlanHelper::assertPlanResponse);
    }

    private void filterPlans_byTraineeId(List<Plan> originalList, Long profileId) throws Exception {
        List<Plan> expectedList = new ArrayList<>(List.of(originalList.get(1), originalList.get(3)));
        PlanFilterRequest request = PlanHelper.createFilterRequest(0, originalList.size(), Plan.Fields.id, Sort.Direction.DESC.name(), null, profileId, null);
        PageResponse<PlanResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Plan::getId,
                PlanResponse::id,
                PlanHelper::assertPlanResponse);
    }

    private void filterPlans_byName(List<Plan> originalList) throws Exception {
        List<Plan> expectedList = new ArrayList<>(List.of(originalList.get(2)));
        String title = expectedList.get(0).getTitle().substring(0, 5);
        PlanFilterRequest request = PlanHelper.createFilterRequest(0, originalList.size(), Plan.Fields.id, Sort.Direction.DESC.name(), null, null, title);
        PageResponse<PlanResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Plan::getId,
                PlanResponse::id,
                PlanHelper::assertPlanResponse);
    }

    private PageResponse<PlanResponse> filter(PlanFilterRequest request) throws Exception {
        return performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPlanById() throws Exception {
        List<Week> weeks = new ArrayList<>();
        List<Goal> goals = new ArrayList<>();

        Plan plan = PlanHelper.createPlan(mockProfile, mockProfile, weeks, goals);
        plan = planRepository.save(plan);

        PlanResponse response = performGet(
                BASE_URL + "/" + plan.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        PlanHelper.assertPlanResponse(plan, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePlan() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        List<Goal> goals = new ArrayList<>(List.of(
                GoalHelper.createGoal()
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks, goals));

        performDeleteNoResponse(
                BASE_URL + "/" + plan.getId(),
                HttpStatus.NO_CONTENT);

        boolean planExists = planRepository.existsById(plan.getId());
        boolean weekExists = weekRepository.existsById(weeks.get(0).getId());
        boolean goalExists = goalRepository.existsById(goals.get(0).getId());

        PlanHelper.assertDelete(planExists, weekExists, goalExists);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPlan() throws Exception {
        User trainee = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile traineeProfile = profileRepository.save(ProfileHelper.createProfile(trainee));

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, traineeProfile));

        List<GoalInputRequest> goals = List.of(GoalHelper.createInputRequest(null, false));
        List<WeekInputRequest> weeks = List.of(WeekHelper.createInputRequest(null, 1, "Note", false));
        PlanInputRequest request = PlanHelper.createInputRequest(
                traineeProfile.getId(),
                goals,
                weeks
        );

        PlanResponse response = performPost(
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
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePlan() throws Exception {
        User trainee1 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile traineeProfile1 = profileRepository.save(ProfileHelper.createProfile(trainee1));
        User trainee2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile traineeProfile2 = profileRepository.save(ProfileHelper.createProfile(trainee2));

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, traineeProfile1));
        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, traineeProfile2));

        // Create plan with initial goals and weeks
        List<Goal> goals = new ArrayList<>(List.of(
                GoalHelper.createGoal(),
                GoalHelper.createGoal()
        ));

        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1),
                WeekHelper.createWeek(2)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, traineeProfile1, weeks, goals));

        Long firstGoalId = plan.getGoals().get(0).getId();
        Long firstWeekId = plan.getWeeks().get(0).getId();

        // Update: keep first goal and first week, remove second goal and second week, add new goal and new week
        List<GoalInputRequest> updatedGoals = List.of(
                GoalHelper.createInputRequest(firstGoalId, true),
                GoalHelper.createInputRequest(null, false)
        );
        List<WeekInputRequest> updatedWeeks = List.of(
                WeekHelper.createInputRequest(firstWeekId, 1, "Week 1 Updated", false),
                WeekHelper.createInputRequest(null, 2, "Week 2 New", false)
        );
        PlanInputRequest request = PlanHelper.createInputRequest(
                traineeProfile2.getId(),
                updatedGoals,
                updatedWeeks
        );

        PlanResponse response = performPut(
                BASE_URL + "/" + plan.getId(),
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Plan updatedPlan = planRepository.getByIdOrThrow(response.id());
        PlanHelper.assertInputToEntity(updatedPlan, request);
        PlanHelper.assertPlanResponse(updatedPlan, response);
    }

}