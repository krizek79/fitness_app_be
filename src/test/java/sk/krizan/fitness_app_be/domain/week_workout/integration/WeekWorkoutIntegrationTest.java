package sk.krizan.fitness_app_be.domain.week_workout.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.helper.RandomHelper;
import sk.krizan.fitness_app_be.domain.coaching_contract.helper.CoachingContractHelper;
import sk.krizan.fitness_app_be.domain.coaching_contract.repository.CoachingContractRepository;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.helper.EquipmentHelper;
import sk.krizan.fitness_app_be.domain.equipment.repository.EquipmentRepository;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.plan.helper.PlanHelper;
import sk.krizan.fitness_app_be.domain.plan.repository.PlanRepository;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.helper.TagHelper;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week.helper.WeekHelper;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;
import sk.krizan.fitness_app_be.domain.week_workout.helper.WeekWorkoutHelper;
import sk.krizan.fitness_app_be.domain.week_workout.repository.WeekWorkoutRepository;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.domain.workout.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.helper.WorkoutExerciseSetHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.repository.WorkoutExerciseSetRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static sk.krizan.fitness_app_be.common.util.DefaultValues.DEFAULT_VALUE;

class WeekWorkoutIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WorkoutExerciseSetRepository workoutExerciseSetRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private WeekWorkoutRepository weekWorkoutRepository;

    @Autowired
    private CoachingContractRepository coachingContractRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private PlanRepository planRepository;


    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;
    private List<Exercise> exercises;

    private static final String BASE_URL = "/week-workouts";

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));

        List<Equipment> equipment = equipmentRepository.saveAll(List.of(
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment()
        ));

        exercises = exerciseRepository.saveAll(ExerciseHelper.createOriginalExercises(equipment));

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWeekWorkout() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks));
        Week week = plan.getWeeks().get(0);

        boolean isWorkoutTemplate = false;
        List<WorkoutExerciseSet> workoutExerciseSets = new ArrayList<>(List.of(WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isWorkoutTemplate)));
        List<WorkoutExercise> workoutExercises = new ArrayList<>(List.of(WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets, 1)));
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, new HashSet<>(), workoutExercises, DEFAULT_VALUE, isWorkoutTemplate));

        WeekWorkout weekWorkout = weekWorkoutRepository.save(WeekWorkoutHelper.createWeekWorkout(week, workout, DayOfWeek.MONDAY));

        performDeleteNoResponse(BASE_URL + "/" + weekWorkout.getId(), HttpStatus.NO_CONTENT);

        boolean weekWorkoutExists = weekWorkoutRepository.existsById(weekWorkout.getId());
        boolean workoutExists = workoutRepository.existsById(workout.getId());
        boolean workoutExerciseExists = workoutExerciseRepository.existsById(workout.getWorkoutExercises().get(0).getId());
        boolean workoutExerciseSetExists = workoutExerciseSetRepository.existsById(workout.getWorkoutExercises().get(0).getWorkoutExerciseSets().get(0).getId());

        WeekWorkoutHelper.assertDelete(weekWorkoutExists, workoutExists, workoutExerciseExists, workoutExerciseSetExists);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createWeekWorkout_withWorkoutFromInput() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks));

        Week week = plan.getWeeks().get(0);

        //  Tag create requests
        Set<TagCreateRequest> tagRequests = new HashSet<>(Set.of(
                TagHelper.createCreateRequest(),
                TagHelper.createCreateRequest()
        ));

        //  Workout exercise set input requests
        List<WorkoutExerciseSetInputRequest> workoutExerciseSetInputRequests = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.WARMUP, false, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 60L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.TOP_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 90L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.WARMUP, false, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 100L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 30L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 3, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 150L)
        ));

        //  Workout exercise input requests
        List<WorkoutExerciseInputRequest> workoutExerciseInputRequests = new ArrayList<>(List.of(
                WorkoutExerciseHelper.createInputRequest(null, exercises.get(0).getId(), 1, workoutExerciseSetInputRequests.subList(0, 2)),
                WorkoutExerciseHelper.createInputRequest(null, exercises.get(exercises.size() - 1).getId(), 2, workoutExerciseSetInputRequests.subList(2, workoutExerciseSetInputRequests.size() - 1))
        ));

        //  Workout input request
        WorkoutInputRequest workoutInputRequest = WorkoutHelper.createInputRequest(
                mockProfile.getId(),
                false,
                tagRequests,
                workoutExerciseInputRequests
        );

        //  Week workout input request
        WeekWorkoutInputRequest weekWorkoutInputRequest = WeekWorkoutHelper.createInputRequest(
                week.getId(),
                null,
                null,
                workoutInputRequest,
                DayOfWeek.TUESDAY,
                1,
                false
        );

        WeekWorkoutResponse response = performPost(
                BASE_URL,
                weekWorkoutInputRequest,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        WeekWorkout createdWeekWorkout = weekWorkoutRepository.getByIdOrThrow(response.id());
        WeekWorkoutHelper.assertInputToEntity(createdWeekWorkout, weekWorkoutInputRequest, null);
        WeekWorkoutHelper.assertWeekWorkoutResponse(createdWeekWorkout, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createWeekWorkout_withClonedWorkout() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks));

        Week week = plan.getWeeks().get(0);

        //  Workout to clone
        Boolean isClonableWorkoutTemplate = true;

        //  Tags of clonable workout
        Set<Tag> tags = new HashSet<>(Set.of(
                TagHelper.createTag()
        ));

        //  Workout exercise sets of clonable workout
        List<WorkoutExerciseSet> workoutExerciseSets = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isClonableWorkoutTemplate),
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isClonableWorkoutTemplate)
        ));

        //  Workout exercises of clonable workout
        List<WorkoutExercise> workoutExercises = new ArrayList<>(List.of(
                WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets.subList(0, 1), 1),
                WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets.subList(1, workoutExerciseSets.size()), 1)
        ));

        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, tags, workoutExercises, "Original template", isClonableWorkoutTemplate));

        WeekWorkoutInputRequest request = WeekWorkoutHelper.createInputRequest(
                week.getId(),
                workout.getId(),
                null,
                null,
                DayOfWeek.MONDAY,
                1,
                false
        );

        WeekWorkoutResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        WeekWorkout createdWeekWorkout = weekWorkoutRepository.getByIdOrThrow(response.id());
        WeekWorkoutHelper.assertInputToEntity(createdWeekWorkout, request, workout);
        WeekWorkoutHelper.assertWeekWorkoutResponse(createdWeekWorkout, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWeekWorkout_withWorkoutFromInput() throws Exception {
        List<Week> weeks = new ArrayList<>(List.of(
                WeekHelper.createWeek(1)
        ));

        Plan plan = planRepository.save(PlanHelper.createPlan(mockProfile, mockProfile, weeks));

        Week week = plan.getWeeks().get(0);

        boolean isWorkoutTemplate = false;

        //  New trainee
        User newTraineeUser = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile newTrainee = profileRepository.save(ProfileHelper.createProfile(newTraineeUser));

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, newTrainee));

        //  --- Original ---

        //  Workout exercise sets
        List<WorkoutExerciseSet> workoutExerciseSets = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isWorkoutTemplate),
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(2, isWorkoutTemplate),
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(3, isWorkoutTemplate)
        ));

        //  Workout exercises
        List<WorkoutExercise> workoutExercises = new ArrayList<>(List.of(
            WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets, 1)
        ));

        Workout workout = WorkoutHelper.createWorkout(mockProfile, new HashSet<>(), workoutExercises, DEFAULT_VALUE, isWorkoutTemplate);
        WeekWorkout weekWorkout = weekWorkoutRepository.save(WeekWorkoutHelper.createWeekWorkout(week, workout, DayOfWeek.MONDAY));

        //  --- Update input request ---

        //  Tag create requests
        Set<TagCreateRequest> tagRequests = new HashSet<>(Set.of(
                TagHelper.createCreateRequest(),
                TagHelper.createCreateRequest(),
                TagHelper.createCreateRequest()
        ));

        //  Workout exercise set input requests
        List<WorkoutExerciseSetInputRequest> workoutExerciseSetInputRequests1 = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(workout.getWorkoutExercises().get(0).getWorkoutExerciseSets().get(0).getId(), WorkoutExerciseSetType.WARMUP, true, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 30L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.TOP_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 90L),
                WorkoutExerciseSetHelper.createInputRequest(workout.getWorkoutExercises().get(0).getWorkoutExerciseSets().get(1).getId(), WorkoutExerciseSetType.BACKOFF_SET, false, 3, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 150L)
        ));

        List<WorkoutExerciseSetInputRequest> workoutExerciseSetInputRequests2 = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.WARMUP, false, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 30L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 60L)

        ));

        //  Workout exercise input requests
        List<WorkoutExerciseInputRequest> workoutExerciseInputRequests = new ArrayList<>(List.of(
            WorkoutExerciseHelper.createInputRequest(workout.getWorkoutExercises().get(0).getId(), exercises.get(0).getId(), 1, workoutExerciseSetInputRequests1),
            WorkoutExerciseHelper.createInputRequest(null, exercises.get(2).getId(), 2, workoutExerciseSetInputRequests2)
        ));

        //  Workout input request
        WorkoutInputRequest workoutInputRequest = WorkoutHelper.createInputRequest(
                newTrainee.getId(),
                isWorkoutTemplate,
                tagRequests,
                workoutExerciseInputRequests
        );

        //  Week workout input request
        WeekWorkoutInputRequest weekWorkoutInputRequest = WeekWorkoutHelper.createInputRequest(
                week.getId(),
                null,
                workout.getId(),
                workoutInputRequest,
                DayOfWeek.TUESDAY,
                1,
                isWorkoutTemplate
        );

        WeekWorkoutResponse response = performPut(
                BASE_URL + "/" + weekWorkout.getId(),
                weekWorkoutInputRequest,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        WeekWorkoutHelper.assertInputToEntity(weekWorkout, weekWorkoutInputRequest, workout);
        WeekWorkoutHelper.assertWeekWorkoutResponse(weekWorkout, response);
    }

}