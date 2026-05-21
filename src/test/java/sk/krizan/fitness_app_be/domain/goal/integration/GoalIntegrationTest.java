package sk.krizan.fitness_app_be.domain.goal.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
import sk.krizan.fitness_app_be.domain.cycle.entity.Level;
import sk.krizan.fitness_app_be.domain.cycle.helper.CycleHelper;
import sk.krizan.fitness_app_be.domain.cycle.repository.CycleRepository;
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.helper.GoalHelper;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class GoalIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    private Profile mockProfile;

    private static final String BASE_URL = "/goals";

    @BeforeEach
    void setUp() {
        User mockUser = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createProfile(mockUser));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterGoals() throws Exception {

        List<Goal> goals1 = new ArrayList<>(List.of(
                GoalHelper.createGoal(),
                GoalHelper.createGoal(),
                GoalHelper.createGoal()
        ));
        List<Goal> goals2 = new ArrayList<>(List.of(
                GoalHelper.createGoal(),
                GoalHelper.createGoal(),
                GoalHelper.createGoal()
        ));

        cycleRepository.save(CycleHelper.createCycle(mockProfile, mockProfile, new ArrayList<>(), goals1, Level.BEGINNER));
        Cycle mockCycle2 = cycleRepository.save(CycleHelper.createCycle(mockProfile, mockProfile, new ArrayList<>(), goals2, Level.BEGINNER));

        List<Goal> expectedList = mockCycle2.getGoals();

        GoalFilterRequest request = GoalHelper.createFilterRequest(0, expectedList.size(), Goal.Fields.id, Sort.Direction.DESC.name(), mockCycle2.getId());

        PageResponse<GoalResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(expectedList, response, Goal::getId, GoalResponse::id, GoalHelper::assertGoalResponse);
    }

}