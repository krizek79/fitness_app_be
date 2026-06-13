package sk.krizan.fitness_app_be.domain.workout_exercise.integration;

import com.fasterxml.jackson.core.type.TypeReference;
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
import sk.krizan.fitness_app_be.domain.exercise.entity.ExerciseCategory;
import sk.krizan.fitness_app_be.domain.exercise.entity.MovementPattern;
import sk.krizan.fitness_app_be.domain.exercise.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRole;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRoleType;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.helper.ExerciseMuscleRoleHelper;
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
    private EquipmentRepository equipmentRepository;

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
        User user = userRepository.save(UserHelper.createUser());
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        when(userService.getOrCreateCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutExerciseById() throws Exception {
        Equipment equipment = equipmentRepository.save(EquipmentHelper.createEquipment());
        ExerciseMuscleRole exerciseMuscleRole = ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.CHEST, ExerciseMuscleRoleType.PRIMARY);

        Exercise exercise = exerciseRepository.save(ExerciseHelper.createExercise(UUID.randomUUID().toString(), ExerciseCategory.STRENGTH, new ArrayList<>(Set.of(MovementPattern.HORIZONTAL_PUSH)), new ArrayList<>(Set.of(exerciseMuscleRole)), new ArrayList<>(Set.of(equipment))));

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