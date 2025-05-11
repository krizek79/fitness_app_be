package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.helper.CloneHelper;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.helper.GoalHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.TagHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WeekHelper;
import sk.krizan.fitness_app_be.helper.WeekWorkoutHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.CycleRepository;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.TagRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class CloneControllerTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private CloneController cloneController;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile("admin", mockUser);
        mockProfile = profileRepository.save(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void cloneCycle() {
        Cycle originalCycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.INTERMEDIATE);
        GoalHelper.createMockGoal(originalCycle);
        Week originalWeek = WeekHelper.createMockWeek(originalCycle, 1);
        Tag originalTag = TagHelper.createMockTag();
        originalTag = tagRepository.save(originalTag);
        Workout originalWorkout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), Set.of(originalTag), UUID.randomUUID().toString());
        Exercise originalExercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        originalExercise = exerciseRepository.save(originalExercise);
        WorkoutExerciseHelper.createMockWorkoutExercise(originalWorkout, originalExercise, 1, 6, Duration.ofMinutes(2));
        WeekWorkoutHelper.createMockWeekWorkout(originalWeek, originalWorkout, 1);
        originalCycle = cycleRepository.save(originalCycle);

        CycleResponse response = cloneController.cloneCycle(originalCycle.getId());
        CloneHelper.assertCycleResponse(originalCycle, response);

        Cycle clonedCycle = cycleRepository.findById(response.id()).orElseThrow(() -> new NotFoundException("Cloned cycle not found."));
        CloneHelper.assertCycle(originalCycle, clonedCycle);
    }

    @Test
    void cloneWorkoutToWeekWorkout() {
        Tag originalTag = TagHelper.createMockTag();
        originalTag = tagRepository.save(originalTag);
        Workout originalWorkout = WorkoutHelper.createMockWorkout(mockProfile, new ArrayList<>(), Set.of(originalTag), UUID.randomUUID().toString());
        Exercise originalExercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        originalExercise = exerciseRepository.save(originalExercise);
        WorkoutExerciseHelper.createMockWorkoutExercise(originalWorkout, originalExercise, 1, 6, Duration.ofMinutes(2));
        originalWorkout = workoutRepository.save(originalWorkout);

        Cycle originalCycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.BEGINNER);
        GoalHelper.createMockGoal(originalCycle);
        Week originalWeek = WeekHelper.createMockWeek(originalCycle, 1);
        cycleRepository.save(originalCycle);

        WeekWorkoutCreateRequest request = WeekWorkoutCreateRequest.builder()
                .workoutId(originalWorkout.getId())
                .weekId(originalWeek.getId())
                .dayOfTheWeek(3)
                .build();
        WeekWorkoutResponse response = cloneController.cloneWorkoutToWeekWorkout(request);

        CloneHelper.assertWorkoutResponse(request, response);
        Workout clonedWorkout = workoutRepository.findByIdAndDeletedFalse(response.workoutId()).orElseThrow(() -> new NotFoundException("Cloned workout not found."));
        CloneHelper.assertWorkout(originalWorkout, clonedWorkout);
        originalCycle = cycleRepository.findById(originalCycle.getId()).orElseThrow(() -> new NotFoundException("Original cycle not found."));
        CloneHelper.assertCycleWorkoutRelation(originalCycle, clonedWorkout);
    }
}