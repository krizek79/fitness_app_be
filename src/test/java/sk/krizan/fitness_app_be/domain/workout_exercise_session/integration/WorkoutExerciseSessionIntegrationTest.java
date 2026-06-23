package sk.krizan.fitness_app_be.domain.workout_exercise_session.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.helper.EquipmentHelper;
import sk.krizan.fitness_app_be.domain.equipment.repository.EquipmentRepository;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.domain.workout.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.helper.WorkoutExerciseSessionHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.repository.WorkoutExerciseSessionRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.response.WorkoutExerciseSessionResponse;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.helper.WorkoutSessionHelper;
import sk.krizan.fitness_app_be.domain.workout_session.repository.WorkoutSessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

class WorkoutExerciseSessionIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    private WorkoutExerciseSessionRepository workoutExerciseSessionRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private List<Exercise> exercises;

    private static final String BASE_URL = "/workout-exercise-sessions";

    @BeforeEach
    void setUp() {
        List<Equipment> equipment = equipmentRepository.saveAll(List.of(
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment()
        ));
        exercises = exerciseRepository.saveAll(ExerciseHelper.createOriginalExercises(equipment));
        User user = userRepository.save(UserHelper.createUser());
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));
        when(userService.getOrCreateCurrentUser()).thenReturn(user);
        when(userService.isUserAdmin(user)).thenReturn(true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseSessionById() throws Exception {
        WorkoutExercise workoutExercise = WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), new ArrayList<>(), 1);
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, Set.of(), List.of(workoutExercise), "Exercise Session Workout", false));
        WorkoutExercise savedExercise = workout.getWorkoutExercises().get(0);

        WorkoutSession workoutSession = workoutSessionRepository.save(WorkoutSessionHelper.createWorkoutSession(workout, null));

        WorkoutExerciseSession exerciseSession = new WorkoutExerciseSession();
        exerciseSession.setWorkoutSession(workoutSession);
        exerciseSession.setWorkoutExercise(savedExercise);
        exerciseSession.setOrder(1);
        workoutSession.addToWorkoutExerciseSessions(exerciseSession);
        WorkoutExerciseSession saved = workoutExerciseSessionRepository.save(exerciseSession);

        WorkoutExerciseSessionResponse response = performGet(
                BASE_URL + "/" + saved.getId(),
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        WorkoutExerciseSessionHelper.assertWorkoutExerciseSessionResponse(saved, response);
    }

}
