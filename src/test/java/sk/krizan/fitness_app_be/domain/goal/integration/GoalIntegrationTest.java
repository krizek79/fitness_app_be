package sk.krizan.fitness_app_be.domain.goal.integration;

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
import sk.krizan.fitness_app_be.domain.goal.entity.Goal;
import sk.krizan.fitness_app_be.domain.goal.helper.GoalHelper;
import sk.krizan.fitness_app_be.domain.goal.repository.GoalRepository;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
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

class GoalIntegrationTest extends BaseIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private static final String BASE_URL = "/goals";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        profileRepository.save(ProfileHelper.createProfile(user));
        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterGoals() throws Exception {
        User user1 = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        Profile profile1 = profileRepository.save(ProfileHelper.createProfile(user1));

        goalRepository.saveAll(List.of(
                GoalHelper.createGoal(profile1, false),
                GoalHelper.createGoal(profile1, true)
        ));

        User user2 = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        Profile profile2 = profileRepository.save(ProfileHelper.createProfile(user2));

        List<Goal> goals2 = goalRepository.saveAll(List.of(
                GoalHelper.createGoal(profile2, false),
                GoalHelper.createGoal(profile2, true)
        ));

        List<Goal> expectedList = new ArrayList<>(List.of(goals2.get(1)));
        GoalFilterRequest request = GoalHelper.createFilterRequest(0, expectedList.size(), Goal.Fields.id, Sort.Direction.DESC.name(), profile2.getId(), expectedList.get(0).getAchieved());

        PageResponse<GoalResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(expectedList, response, Goal::getId, GoalResponse::id, GoalHelper::assertGoalResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGoal() throws Exception {
        GoalInputRequest request = GoalHelper.createInputRequest(false);

        GoalResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Goal createdGoal = goalRepository.findById(response.id()).orElseThrow();
        GoalHelper.assertInputToEntity(createdGoal, request);
        GoalHelper.assertGoalResponse(createdGoal, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGoal() throws Exception {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        Profile profile = profileRepository.save(ProfileHelper.createProfile(user));

        Goal goal = goalRepository.save(GoalHelper.createGoal(profile, false));

        GoalInputRequest request = GoalHelper.createInputRequest(true);

        GoalResponse response = performPut(
                BASE_URL + "/" + goal.getId(),
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Goal updatedGoal = goalRepository.findById(response.id()).orElseThrow();
        GoalHelper.assertInputToEntity(updatedGoal, request);
        GoalHelper.assertGoalResponse(updatedGoal, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGoal() throws Exception {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        Profile profile = profileRepository.save(ProfileHelper.createProfile(user));

        Goal goal = goalRepository.save(GoalHelper.createGoal(profile, false));

        performDeleteNoResponse(BASE_URL + "/" + goal.getId(), HttpStatus.NO_CONTENT);

        boolean goalExists = goalRepository.existsById(goal.getId());
        Assertions.assertFalse(goalExists);
    }

}