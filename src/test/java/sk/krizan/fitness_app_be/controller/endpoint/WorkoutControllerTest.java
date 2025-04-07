package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class WorkoutControllerTest {

    @Autowired
    private WorkoutController workoutController;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        mockProfile = profileRepository.save(mockProfile);
        mockUser.setProfile(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    //  TODO
    @Test
    @Disabled
    void filterWorkouts() {
    }

    @Test
    void getWorkoutById() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        workout = workoutRepository.save(workout);

        WorkoutResponse response = workoutController.getWorkoutById(workout.getId());
        WorkoutHelper.assertWorkoutResponse_get(workout, response);
    }

    @Test
    void createWorkout() {
        WorkoutCreateRequest createRequest = WorkoutHelper.createCreateRequest();
        WorkoutResponse response = workoutController.createWorkout(createRequest);
        WorkoutHelper.assertWorkoutResponse_create(createRequest, mockProfile.getName(), response);
    }

    @Test
    void updateWorkout() {
        Workout mockWorkout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        Workout savedMockWorkout = workoutRepository.save(mockWorkout);
        WorkoutUpdateRequest updateRequest = WorkoutHelper.createUpdateRequest();

        WorkoutResponse response = workoutController.updateWorkout(savedMockWorkout.getId(), updateRequest);

        WorkoutHelper.assertWorkoutResponse_update(updateRequest, mockProfile.getName(), response);
    }

    @Test
    void deleteWorkout() {
        Workout mockWorkout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        Workout savedMockWorkout = workoutRepository.save(mockWorkout);

        Long deletedWorkoutId = workoutController.deleteWorkout(savedMockWorkout.getId());
        boolean exists = workoutRepository.findByIdAndDeletedFalse(deletedWorkoutId).isPresent();

        WorkoutHelper.assertDelete(exists, savedMockWorkout, deletedWorkoutId);
    }
}