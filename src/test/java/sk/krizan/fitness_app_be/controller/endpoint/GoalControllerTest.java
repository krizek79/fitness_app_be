package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.GoalHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Goal;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.GoalRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class GoalControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalController goalController;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private Cycle mockCycle;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser(Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile(mockUser);
        mockProfile = profileRepository.save(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        mockCycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        mockCycle = cycleRepository.save(mockCycle);
    }

    @Test
    void filterGoals() {
        Cycle mockCycle2 = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        mockCycle2 = cycleRepository.save(mockCycle2);
        List<Goal> originalList = GoalHelper.createMockGoalList(new ArrayList<>(List.of(mockCycle, mockCycle2)));
        List<Goal> savedGoalList = goalRepository.saveAll(originalList);
        List<Goal> expectedList = savedGoalList.stream().filter(goal -> goal.getCycle().getId().equals(mockCycle.getId())).collect(Collectors.toList());

        GoalFilterRequest request = GoalHelper.createFilterRequest(0, originalList.size(), Goal.Fields.id, Sort.Direction.DESC.name(), mockCycle.getId());
        PageResponse<GoalResponse> response = goalController.filterGoals(request);
        GoalHelper.assertFilter(expectedList, request, response);
    }

    @Test
    void getGoalById() {
        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);
        GoalResponse response = goalController.getGoalById(goal.getId());
        GoalHelper.assertGoalResponse_get(goal, response);
    }

    @Test
    void createGoal() {
        GoalCreateRequest request = GoalHelper.createCreateRequest(mockCycle.getId());
        GoalResponse response = goalController.createGoal(request);
        GoalHelper.assertGoalResponse_create(request, response);
    }

    @Test
    void updateGoal() {
        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);
        GoalUpdateRequest request = GoalHelper.createUpdateRequest();
        GoalResponse response = goalController.updateGoal(goal.getId(), request);
        GoalHelper.assertGoalResponse_update(request, response);
    }

    @Test
    void deleteGoal() {
        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);

        Long deletedGoalId = goalController.deleteGoal(goal.getId());
        boolean exists = goalRepository.existsById(deletedGoalId);

        GoalHelper.assertDelete(exists, goal, deletedGoalId);
    }

    @Test
    void triggerAchieved() {
        Goal goal = GoalHelper.createMockGoal(mockCycle);
        goal = goalRepository.save(goal);
        Boolean originalState = goal.getAchieved();

        GoalResponse response = goalController.triggerAchieved(goal.getId());
        GoalHelper.assertTriggerAchieved(originalState, response);
    }
}