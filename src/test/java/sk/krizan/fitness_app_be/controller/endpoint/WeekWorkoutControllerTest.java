package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WeekHelper;
import sk.krizan.fitness_app_be.helper.WeekWorkoutHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WeekRepository;
import sk.krizan.fitness_app_be.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class WeekWorkoutControllerTest {

    @Autowired
    private WeekWorkoutController weekWorkoutController;

    @Autowired
    private WeekWorkoutRepository weekWorkoutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private WeekRepository weekRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private Cycle mockCycle;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        mockProfile = profileRepository.save(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);

        mockCycle = CycleHelper.createMockCycle(mockProfile, mockProfile);
        mockCycle = cycleRepository.save(mockCycle);
    }

    @Test
    void filterWeekWorkouts() {
        Week week1 = WeekHelper.createMockWeek(mockCycle, 1);
        week1 = weekRepository.save(week1);
        Workout workout1 = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        workout1 = workoutRepository.save(workout1);
        WeekWorkout weekWorkout1 = WeekWorkoutHelper.createMockWeekWorkout(week1, workout1, 1);
        weekWorkout1 = weekWorkoutRepository.save(weekWorkout1);

        Week week2 = WeekHelper.createMockWeek(mockCycle, 2);
        week2 = weekRepository.save(week2);
        Workout workout2 = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        workout2 = workoutRepository.save(workout2);
        WeekWorkout weekWorkout2 = WeekWorkoutHelper.createMockWeekWorkout(week2, workout2, 2);
        weekWorkout2 = weekWorkoutRepository.save(weekWorkout2);

        List<WeekWorkout> originalList = new ArrayList<>(List.of(weekWorkout1, weekWorkout2));
        List<WeekWorkout> expectedList = new ArrayList<>(List.of(originalList.get(0)));

        WeekWorkoutFilterRequest request = WeekWorkoutHelper.createFilterRequest(0, originalList.size(), WeekWorkout.Fields.id, Sort.Direction.DESC.name(), expectedList.get(0).getWeek().getId());
        PageResponse<WeekWorkoutResponse> response = weekWorkoutController.filterWeekWorkouts(request);

        WeekWorkoutHelper.assertFilter(expectedList, request, response);
    }

    @Test
    void getWeekWorkoutById() {
        Week week = WeekHelper.createMockWeek(mockCycle, 1);
        week = weekRepository.save(week);
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        workout = workoutRepository.save(workout);
        WeekWorkout weekWorkout = WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1);
        weekWorkout = weekWorkoutRepository.save(weekWorkout);

        WeekWorkoutResponse response = weekWorkoutController.getWeekWorkoutById(weekWorkout.getId());
        WeekWorkoutHelper.assertWeekWorkoutResponse_get(weekWorkout, response);
    }

    @Test
    void createWeekWorkout() {
        Week week = WeekHelper.createMockWeek(mockCycle, 1);
        week = weekRepository.save(week);
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        workout = workoutRepository.save(workout);

        WeekWorkoutCreateRequest request = WeekWorkoutHelper.createCreateRequest(week.getId(), workout.getId(), 3);
        WeekWorkoutResponse response = weekWorkoutController.createWeekWorkout(request);

        WeekWorkout createdWeekWorkout = weekWorkoutRepository.findById(response.id()).orElseThrow();
        WeekWorkoutHelper.assertWeekWorkout_create(request, response, createdWeekWorkout);
    }

    @Test
    void updateWeekWorkout() {
        Week week = WeekHelper.createMockWeek(mockCycle, 1);
        week = weekRepository.save(week);
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        workout = workoutRepository.save(workout);
        WeekWorkout weekWorkout = WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1);
        weekWorkout = weekWorkoutRepository.save(weekWorkout);

        WeekWorkoutUpdateRequest request = WeekWorkoutHelper.createUpdateRequest(2);
        WeekWorkoutResponse response = weekWorkoutController.updateWeekWorkout(weekWorkout.getId(), request);
        WeekWorkout updatedWeekWorkout = weekWorkoutRepository.findById(weekWorkout.getId()).orElseThrow();
        WeekWorkoutHelper.assertWeekWorkout_update(request, response, updatedWeekWorkout);
    }

    @Test
    void deleteWeekWorkout() {
        Week week = WeekHelper.createMockWeek(mockCycle, 1);
        week = weekRepository.save(week);
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        workout = workoutRepository.save(workout);
        WeekWorkout weekWorkout = WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1);
        weekWorkout = weekWorkoutRepository.save(weekWorkout);

        Long deletedWeekWorkoutId = weekWorkoutController.deleteWeekWorkout(weekWorkout.getId());
        boolean exists = weekWorkoutRepository.existsById(deletedWeekWorkoutId);

        WeekWorkoutHelper.assertDelete(exists, weekWorkout, deletedWeekWorkoutId);
    }

    @Test
    void triggerCompleted() {
        Week week = weekRepository.save(WeekHelper.createMockWeek(mockCycle, 1));
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>()));
        WeekWorkout WeekWorkout = WeekWorkoutHelper.createMockWeekWorkout(week, workout, 1);
        WeekWorkout = weekWorkoutRepository.save(WeekWorkout);
        Boolean originalState = WeekWorkout.getCompleted();

        WeekWorkoutResponse response = weekWorkoutController.triggerCompleted(WeekWorkout.getId());
        WeekWorkoutHelper.assertTriggerCompleted(originalState, response);
    }
}