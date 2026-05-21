package sk.krizan.fitness_app_be.domain.cycle.integration;

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
import sk.krizan.fitness_app_be.domain.coach_client.helper.CoachClientHelper;
import sk.krizan.fitness_app_be.domain.coach_client.repository.CoachClientRepository;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.cycle.entity.Level;
import sk.krizan.fitness_app_be.domain.cycle.helper.CycleHelper;
import sk.krizan.fitness_app_be.domain.cycle.repository.CycleRepository;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.request.CycleInputRequest;
import sk.krizan.fitness_app_be.domain.cycle.rest.dto.response.CycleResponse;
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

class CycleIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CoachClientRepository coachClientRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private WeekRepository weekRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    private static final String BASE_URL = "/cycles";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterCycles() throws Exception {
        User user1 = UserHelper.createUser(Set.of(Role.USER));
        user1 = userRepository.save(user1);
        Profile profile1 = ProfileHelper.createProfile(user1);
        profile1 = profileRepository.save(profile1);
        User user2 = UserHelper.createUser(Set.of(Role.USER));
        user2 = userRepository.save(user2);
        Profile profile2 = ProfileHelper.createProfile(user2);
        profile2 = profileRepository.save(profile2);

        List<Cycle> originalList = CycleHelper.createMockCycleListForFilter(profile1, profile2);
        originalList = cycleRepository.saveAll(originalList);

        filterCycles_byAuthorId(originalList, profile1.getId());
        filterCycles_byTraineeId(originalList, profile2.getId());
        filterCycles_byName(originalList);
        filterCycles_byLevelKey(originalList);
    }

    private void filterCycles_byAuthorId(List<Cycle> originalList, Long profileId) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(0), originalList.get(1)));
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), profileId, null, null, null);
        PageResponse<CycleResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Cycle::getId,
                CycleResponse::id,
                CycleHelper::assertCycleResponse);
    }

    private void filterCycles_byTraineeId(List<Cycle> originalList, Long profileId) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(1), originalList.get(3)));
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, profileId, null, null);
        PageResponse<CycleResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Cycle::getId,
                CycleResponse::id,
                CycleHelper::assertCycleResponse);
    }

    private void filterCycles_byName(List<Cycle> originalList) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(2)));
        String name = expectedList.get(0).getTitle().substring(0, 5);
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, null, name, null);
        PageResponse<CycleResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Cycle::getId,
                CycleResponse::id,
                CycleHelper::assertCycleResponse);
    }

    private void filterCycles_byLevelKey(List<Cycle> originalList) throws Exception {
        List<Cycle> expectedList = new ArrayList<>(List.of(originalList.get(3)));
        Level level = expectedList.get(0).getLevel();
        CycleFilterRequest request = CycleHelper.createFilterRequest(0, originalList.size(), Cycle.Fields.id, Sort.Direction.DESC.name(), null, null, null, level);
        PageResponse<CycleResponse> response = filter(request);
        FilterAssertionUtils.assertFilterResults(
                expectedList,
                response,
                Cycle::getId,
                CycleResponse::id,
                CycleHelper::assertCycleResponse);
    }

    private PageResponse<CycleResponse> filter(CycleFilterRequest request) throws Exception {
        return performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCycleById() throws Exception {
        List<Week> weeks = new ArrayList<>();
        List<Goal> goals = new ArrayList<>();

        Cycle cycle = CycleHelper.createCycle(mockProfile, mockProfile, weeks, goals, Level.BEGINNER);
        cycle = cycleRepository.save(cycle);

        CycleResponse response = performGet(
                BASE_URL + "/" + cycle.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        CycleHelper.assertCycleResponse(cycle, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCycle() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        List<Goal> goals = new ArrayList<>(List.of(
                GoalHelper.createGoal()
        ));

        Cycle cycle = cycleRepository.save(CycleHelper.createCycle(mockProfile, mockProfile, weeks, goals, Level.INTERMEDIATE));

        performDeleteNoResponse(
                BASE_URL + "/" + cycle.getId(),
                HttpStatus.NO_CONTENT);

        boolean cycleExists = cycleRepository.existsById(cycle.getId());
        boolean weekExists = weekRepository.existsById(weeks.get(0).getId());
        boolean goalExists = goalRepository.existsById(goals.get(0).getId());

        Assertions.assertFalse(cycleExists);
        Assertions.assertFalse(weekExists);
        Assertions.assertFalse(goalExists);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCycle() throws Exception {
        User trainee = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile traineeProfile = profileRepository.save(ProfileHelper.createProfile(trainee));

        coachClientRepository.save(CoachClientHelper.createCoachClient(mockProfile, traineeProfile));

        List<GoalInputRequest> goals = List.of(GoalHelper.createInputRequest(null, false));
        List<WeekInputRequest> weeks = List.of(WeekHelper.createInputRequest(null, 1, "Note", false));
        CycleInputRequest request = CycleHelper.createInputRequest(
                traineeProfile.getId(),
                Level.BEGINNER,
                goals,
                weeks
        );

        CycleResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Cycle savedCycle = cycleRepository.getByIdOrThrow(response.id());
        CycleHelper.assertInputToEntity(savedCycle, request);
        Assertions.assertEquals(traineeProfile.getId(), savedCycle.getTrainee().getId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCycle() throws Exception {
        User trainee1 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile traineeProfile1 = profileRepository.save(ProfileHelper.createProfile(trainee1));
        User trainee2 = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile traineeProfile2 = profileRepository.save(ProfileHelper.createProfile(trainee2));

        coachClientRepository.save(CoachClientHelper.createCoachClient(mockProfile, traineeProfile1));
        coachClientRepository.save(CoachClientHelper.createCoachClient(mockProfile, traineeProfile2));

        // Create cycle with initial goals and weeks
        List<Goal> goals = new ArrayList<>(List.of(
                GoalHelper.createGoal(),
                GoalHelper.createGoal()
        ));

        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1),
                WeekHelper.createWeek(2)
        ));

        Cycle cycle = cycleRepository.save(CycleHelper.createCycle(mockProfile, traineeProfile1, weeks, goals, Level.INTERMEDIATE));

        Long firstGoalId = cycle.getGoals().get(0).getId();
        Long firstWeekId = cycle.getWeeks().get(0).getId();

        // Update: keep first goal and first week, remove second goal and second week, add new goal and new week
        List<GoalInputRequest> updatedGoals = List.of(
                GoalHelper.createInputRequest(firstGoalId, true),
                GoalHelper.createInputRequest(null, false)
        );
        List<WeekInputRequest> updatedWeeks = List.of(
                WeekHelper.createInputRequest(firstWeekId, 1, "Week 1 Updated", false),
                WeekHelper.createInputRequest(null, 2, "Week 2 New", false)
        );
        CycleInputRequest request = CycleHelper.createInputRequest(
                traineeProfile2.getId(),
                Level.ADVANCED,
                updatedGoals,
                updatedWeeks
        );

        CycleResponse response = performPut(
                BASE_URL + "/" + cycle.getId(),
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Cycle updatedCycle = cycleRepository.getByIdOrThrow(response.id());
        CycleHelper.assertInputToEntity(updatedCycle, request);
        CycleHelper.assertCycleResponse(updatedCycle, response);
    }

}