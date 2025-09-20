package sk.krizan.fitness_app_be.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.helper.CloneHelper;
import sk.krizan.fitness_app_be.helper.CycleHelper;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.helper.GoalHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.TagHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WeekHelper;
import sk.krizan.fitness_app_be.helper.WeekWorkoutHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseSetHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Cycle;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
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

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CloneControllerTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = userRepository.save(UserHelper.createMockUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createMockProfile(mockUser));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cloneCycle() throws Exception {
        when(userService.getCurrentUser()).thenReturn(mockUser);

        Cycle originalCycle = CycleHelper.createMockCycle(mockProfile, mockProfile, Level.INTERMEDIATE);
        GoalHelper.createMockGoal(originalCycle);
        Week originalWeek = WeekHelper.createMockWeek(originalCycle, 1);
        Tag originalTag = TagHelper.createMockTag();
        originalTag = tagRepository.save(originalTag);
        Workout originalWorkout = WorkoutHelper.createMockWorkout(mockProfile, Set.of(originalTag), UUID.randomUUID().toString());
        Exercise originalExercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        originalExercise = exerciseRepository.save(originalExercise);
        WorkoutExerciseHelper.createMockWorkoutExercise(originalWorkout, originalExercise, 1);
        WeekWorkoutHelper.createMockWeekWorkout(originalWeek, originalWorkout, 1);
        originalCycle = cycleRepository.save(originalCycle);

        MvcResult mvcResult = mockMvc.perform(
                        post("/clone/cycle/" + originalCycle.getId())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CycleResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
        CloneHelper.assertCycleResponse(originalCycle, response);

        Cycle clonedCycle = cycleRepository.findById(response.id()).orElseThrow();
        CloneHelper.assertCycle(originalCycle, clonedCycle);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cloneWorkoutToWeekWorkout() throws Exception {
        Tag originalTag = tagRepository.save(TagHelper.createMockTag());
        Workout originalWorkout = WorkoutHelper.createMockWorkout(mockProfile, Set.of(originalTag), UUID.randomUUID().toString());
        Exercise originalExercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS)));
        WorkoutExercise originalWorkoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(originalWorkout, originalExercise, 1);
        WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(originalWorkoutExercise, 1);
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

        MvcResult mvcResult = mockMvc.perform(
                        post("/clone/workout-to-week-workout")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        WeekWorkoutResponse response = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        CloneHelper.assertWorkoutResponse(request, response);
        Workout clonedWorkout = workoutRepository.findById(response.workoutId()).orElseThrow();
        CloneHelper.assertWorkout(originalWorkout, clonedWorkout);
        originalCycle = cycleRepository.findById(originalCycle.getId()).orElseThrow();
        CloneHelper.assertCycleWorkoutRelation(originalCycle, clonedWorkout);
    }
}