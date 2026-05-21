package sk.krizan.fitness_app_be.domain.workout_exercise.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;
import sk.krizan.fitness_app_be.domain.exercise.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.domain.workout.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.helper.WorkoutExerciseSetHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static sk.krizan.fitness_app_be.common.util.DefaultValues.DEFAULT_VALUE;

class WorkoutExerciseIntegrationTest extends BaseIntegrationTest {

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

    private static final String BASE_URL = "/workout-exercises";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseById() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS)));

        Boolean isWorkoutTemplate = false;
        List<WorkoutExerciseSet> workoutExerciseSets = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isWorkoutTemplate))
        );
        List<WorkoutExercise> workoutExercises = new ArrayList<>(List.of(
                WorkoutExerciseHelper.createWorkoutExercise(exercise, workoutExerciseSets, 1)
        ));
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, new HashSet<>(), workoutExercises, DEFAULT_VALUE, isWorkoutTemplate));

        WorkoutExercise workoutExercise = workout.getWorkoutExercises().get(0);

        WorkoutExerciseResponse response = performGet(
                BASE_URL + "/" + workoutExercise.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        WorkoutExerciseHelper.assertWorkoutExerciseResponse(workoutExercise, response);
    }

}