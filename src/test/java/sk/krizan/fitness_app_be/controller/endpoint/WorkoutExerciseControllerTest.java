package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@Transactional
@SpringBootTest
class WorkoutExerciseControllerTest {

    @Autowired
    private WorkoutExerciseController workoutExerciseController;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

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

    @Test
    @Disabled
    void filterWorkoutExercises() {
        //  TODO
    }

    @Test
    void getWorkoutExerciseById() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);
        WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 2, 6, Duration.ofMinutes(3));
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

        WorkoutExerciseResponse response = workoutExerciseController.getWorkoutExerciseById(workoutExercise.getId());
        WorkoutExerciseHelper.assertGet(workoutExercise, response);
    }

    @Test
    void createWorkoutExercise() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);

        WorkoutExerciseCreateRequest request = WorkoutExerciseHelper.createCreateRequest(workout.getId(), exercise.getId(), 8, 2, "PT3M");
        WorkoutExerciseResponse response = workoutExerciseController.createWorkoutExercise(request);

        WorkoutExerciseHelper.assertCreate(request, exercise.getName(), response);
    }

    @Test
    void updateWorkoutExercise() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);
        WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 2, 6, Duration.ofMinutes(3));
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

        WorkoutExerciseUpdateRequest request = WorkoutExerciseHelper.createUpdateRequest(workoutExercise.getId(), 7, 1, "PT4M");
        WorkoutExerciseResponse response = workoutExerciseController.updateWorkoutExercise(request);

        WorkoutExerciseHelper.assertUpdate(request, response);
    }

    @Test
    void deleteWorkoutExercise() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);
        WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 2, 6, Duration.ofMinutes(3));
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

        Long deletedWorkoutExerciseId = workoutExerciseController.deleteWorkoutExercise(workoutExercise.getId());
        boolean exists = workoutExerciseRepository.existsById(deletedWorkoutExerciseId);

        WorkoutExerciseHelper.assertDelete(exists, workoutExercise, deletedWorkoutExerciseId);
    }
}