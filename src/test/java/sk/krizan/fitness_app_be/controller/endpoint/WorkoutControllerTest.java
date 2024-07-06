package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest
class WorkoutControllerTest {

    @Autowired
    private WorkoutController workoutController;

    @Autowired
    private WorkoutRepository workoutRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        mockUser.setProfile(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void filterWorkouts() {
    }

    @Test
    void getWorkoutById() {
    }

    @Test
    @Transactional
    void createWorkout() {
        WorkoutCreateRequest createRequest = WorkoutHelper.createCreateRequest();
        WorkoutDetailResponse response = workoutController.createWorkout(createRequest);
        WorkoutHelper.assertWorkoutDetailResponse_create(createRequest, mockProfile.getName(), response);
    }

    @Test
    @Transactional
    void updateWorkout() {
        Workout mockWorkout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        Workout savedMockWorkout = workoutRepository.save(mockWorkout);
        WorkoutUpdateRequest updateRequest = WorkoutHelper.createUpdateRequest();

        WorkoutDetailResponse response = workoutController.updateWorkout(savedMockWorkout.getId(), updateRequest);

        WorkoutHelper.assertWorkoutDetailResponse_update(updateRequest, mockProfile.getName(), response);
    }

    @Test
    @Transactional
    void deleteWorkout() {
        Workout mockWorkout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), new HashSet<>());
        Workout savedMockWorkout = workoutRepository.save(mockWorkout);

        Long deletedWorkoutId = workoutController.deleteWorkout(savedMockWorkout.getId());
        boolean exists = workoutRepository.existsById(deletedWorkoutId);

        WorkoutHelper.assertDelete(exists, savedMockWorkout, deletedWorkoutId);
    }
}