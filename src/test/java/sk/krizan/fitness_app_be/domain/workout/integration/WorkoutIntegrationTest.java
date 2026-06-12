package sk.krizan.fitness_app_be.domain.workout.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.helper.RandomHelper;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.coaching_contract.helper.CoachingContractHelper;
import sk.krizan.fitness_app_be.domain.coaching_contract.repository.CoachingContractRepository;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.helper.EquipmentHelper;
import sk.krizan.fitness_app_be.domain.equipment.repository.EquipmentRepository;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.profile.repository.ProfileRepository;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.helper.TagHelper;
import sk.krizan.fitness_app_be.domain.tag.repository.TagRepository;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.helper.UserHelper;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.domain.workout.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static sk.krizan.fitness_app_be.common.util.DefaultValues.DEFAULT_VALUE;

class WorkoutIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WorkoutExerciseSetRepository workoutExerciseSetRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private CoachingContractRepository coachingContractRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @MockBean
    private UserService userService;

    private Profile mockProfile;

    private List<Exercise> exercises;

    private static final String BASE_URL = "/workouts";

    @BeforeEach
    void setUp() {
        List<Equipment> equipment = equipmentRepository.saveAll(List.of(
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment()
        ));
        exercises = exerciseRepository.saveAll(ExerciseHelper.createOriginalExercises(equipment));
        User user = userRepository.save(UserHelper.createUser(Set.of(Role.ADMIN)));
        mockProfile = profileRepository.save(ProfileHelper.createProfile(user));
        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterWorkouts() throws Exception {
        List<Profile> profileList = new ArrayList<>();
        List<List<WorkoutExercise>> workoutExerciseList = new ArrayList<>();
        List<Set<Tag>> tagSetList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            User user = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
            Profile profile = profileRepository.save(ProfileHelper.createProfile(user));
            profileList.add(profile);

            workoutExerciseList.add(new ArrayList<>());

            Tag tag1 = tagRepository.save(TagHelper.createTag());
            Tag tag2 = tagRepository.save(TagHelper.createTag());
            tagSetList.add(Set.of(tag1, tag2));
        }

        List<Workout> originalWorkoutList = new ArrayList<>();
        try {
            originalWorkoutList = WorkoutHelper.createWorkoutList(profileList, workoutExerciseList, tagSetList);
        } catch (Exception e) {
            Assertions.fail("An error occurred while creating sample data.");
        }

        filterWorkouts_ByName(originalWorkoutList);
        filterWorkouts_ByTagNameList(originalWorkoutList);
        filterWorkouts_ByAuthorId(originalWorkoutList);
        filterWorkouts_ByIsTemplate(originalWorkoutList);
    }

    private void filterWorkouts_ByAuthorId(List<Workout> originalWorkoutList) throws Exception {
        List<Workout> expectedWorkouts = List.of(originalWorkoutList.get(1));

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), expectedWorkouts.get(0).getAuthor().getId(), null, null, false);
        PageResponse<WorkoutSimpleResponse> response = filter(request);

        FilterAssertionUtils.assertFilterResults(expectedWorkouts, response, Workout::getId, WorkoutSimpleResponse::id, WorkoutHelper::assertWorkoutSimpleResponse);
    }

    private void filterWorkouts_ByTagNameList(List<Workout> originalWorkoutList) throws Exception {
        List<Workout> expectedWorkouts = List.of(originalWorkoutList.get(2));

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, expectedWorkouts.get(0).getTags().stream().map(Tag::getId).collect(Collectors.toList()), null, false);
        PageResponse<WorkoutSimpleResponse> response = filter(request);

        FilterAssertionUtils.assertFilterResults(expectedWorkouts, response, Workout::getId, WorkoutSimpleResponse::id, WorkoutHelper::assertWorkoutSimpleResponse);
    }

    private void filterWorkouts_ByName(List<Workout> originalWorkoutList) throws Exception {
        List<Workout> expectedWorkouts = List.of(originalWorkoutList.get(0));

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, null, expectedWorkouts.get(0).getTitle().substring(0, 5), false);
        PageResponse<WorkoutSimpleResponse> response = filter(request);

        FilterAssertionUtils.assertFilterResults(expectedWorkouts, response, Workout::getId, WorkoutSimpleResponse::id, WorkoutHelper::assertWorkoutSimpleResponse);
    }

    private void filterWorkouts_ByIsTemplate(List<Workout> originalWorkoutList) throws Exception {
        List<Workout> expectedWorkouts = List.of(originalWorkoutList.get(3));

        WorkoutFilterRequest request = WorkoutHelper.createFilterRequest(0, originalWorkoutList.size(), Workout.Fields.id, Sort.Direction.DESC.name(), null, null, null, true);
        PageResponse<WorkoutSimpleResponse> response = filter(request);

        FilterAssertionUtils.assertFilterResults(expectedWorkouts, response, Workout::getId, WorkoutSimpleResponse::id, WorkoutHelper::assertWorkoutSimpleResponse);
    }

    private PageResponse<WorkoutSimpleResponse> filter(WorkoutFilterRequest request) throws Exception {
        return performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutById() throws Exception {
        Boolean isClonableWorkoutTemplate = true;

        Set<Tag> tags = new HashSet<>(Set.of(
                TagHelper.createTag()
        ));

        //  Workout exercise sets of clonable workout
        List<WorkoutExerciseSet> workoutExerciseSets = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isClonableWorkoutTemplate),
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(2, isClonableWorkoutTemplate)
        ));

        //  Workout exercises of clonable workout
        List<WorkoutExercise> workoutExercises = new ArrayList<>(List.of(
                WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets.subList(0, 1), 1),
                WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets.subList(1, workoutExerciseSets.size()), 1)
        ));

        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, tags, workoutExercises, DEFAULT_VALUE, isClonableWorkoutTemplate));

        WorkoutDetailResponse response = performGet(
                BASE_URL + "/" + workout.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        WorkoutHelper.assertWorkoutDetailResponse(workout, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWorkout() throws Exception {
        boolean isWorkoutTemplate = false;

        List<WorkoutExerciseSet> workoutExerciseSets = new ArrayList<>(List.of(WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isWorkoutTemplate)));
        List<WorkoutExercise> workoutExercises = new ArrayList<>(List.of(WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets, 1)));
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, new HashSet<>(), workoutExercises, DEFAULT_VALUE, isWorkoutTemplate));

        performDeleteNoResponse(BASE_URL + "/" + workout.getId(), HttpStatus.NO_CONTENT);

        boolean workoutExists = workoutRepository.existsById(workout.getId());
        boolean workoutExerciseExists = workoutExerciseRepository.existsById(workout.getWorkoutExercises().get(0).getId());
        boolean workoutExerciseSetExists = workoutExerciseSetRepository.existsById(workout.getWorkoutExercises().get(0).getWorkoutExerciseSets().get(0).getId());

        WorkoutHelper.assertDelete(workoutExists, workoutExerciseExists, workoutExerciseSetExists);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createWorkout_template() throws Exception {
        Boolean isWorkoutTemplate = true;

        //  Tag create requests
        Set<TagCreateRequest> tags = new HashSet<>(Set.of(
                TagHelper.createCreateRequest()
        ));

        //  Workout exercise set input requests
        List<WorkoutExerciseSetInputRequest> workoutExerciseSets1 = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.WARMUP, false, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 30L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 90L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 3, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 150L)
        ));

        List<WorkoutExerciseSetInputRequest> workoutExerciseSets2 = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.WARMUP, false, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 30L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.TOP_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 90L)
        ));

        List<WorkoutExerciseSetInputRequest> workoutExerciseSets3 = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 1, null, null, RandomHelper.getRandomInt(1, 8), null, 1500L, null, 300L)
        ));

        //  Workout exercise input requests
        List<WorkoutExerciseInputRequest> workoutExerciseInputRequests = new ArrayList<>(List.of(
                WorkoutExerciseHelper.createInputRequest(null, exercises.get(0).getId(), 1, workoutExerciseSets1),
                WorkoutExerciseHelper.createInputRequest(null, exercises.get(3).getId(), 2, workoutExerciseSets2),
                WorkoutExerciseHelper.createInputRequest(null, exercises.get(4).getId(), 3, workoutExerciseSets3)
        ));

        WorkoutInputRequest request = WorkoutHelper.createInputRequest(mockProfile.getId(), isWorkoutTemplate, tags, workoutExerciseInputRequests);

        WorkoutDetailResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Workout workout = workoutRepository.getByIdOrThrow(response.id());
        WorkoutHelper.assertInputToEntity(workout, request);
        WorkoutHelper.assertWorkoutDetailResponse(workout, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWorkout() throws Exception {
        User newTrainee = userRepository.save(UserHelper.createUser(Set.of(Role.USER)));
        Profile newTraineeProfile = profileRepository.save(ProfileHelper.createProfile(newTrainee));

        coachingContractRepository.save(CoachingContractHelper.createCoachingContract(mockProfile, newTraineeProfile));

        Boolean isWorkoutTemplate = false;

        //  Original workout

        Set<Tag> tags = new HashSet<>(Set.of(
                TagHelper.createTag()
        ));

        List<WorkoutExerciseSet> workoutExerciseSets = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(1, isWorkoutTemplate),
                WorkoutExerciseSetHelper.createWorkoutExerciseSet(2, isWorkoutTemplate)
        ));

        List<WorkoutExercise> workoutExercises = new ArrayList<>(List.of(
                WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), workoutExerciseSets, 1)
        ));

        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, tags, workoutExercises, DEFAULT_VALUE, isWorkoutTemplate));

        //  Workout input request

        Set<TagCreateRequest> tagCreateRequests = new HashSet<>(Set.of(
                TagHelper.createCreateRequest(),
                TagHelper.createCreateRequest(),
                TagHelper.createCreateRequest()
        ));

        List<WorkoutExerciseSetInputRequest> workoutExerciseSetInputRequests1 = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.WARMUP, false, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 30L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 90L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.STRAIGHT_SET, false, 3, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 150L)
        ));

        List<WorkoutExerciseSetInputRequest> workoutExerciseSetInputRequests2 = new ArrayList<>(List.of(
                WorkoutExerciseSetHelper.createInputRequest(workout.getWorkoutExercises().get(0).getWorkoutExerciseSets().get(0).getId(), WorkoutExerciseSetType.WARMUP, false, 1, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 30L),
                WorkoutExerciseSetHelper.createInputRequest(null, WorkoutExerciseSetType.TOP_SET, false, 2, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 90L),
                WorkoutExerciseSetHelper.createInputRequest(workout.getWorkoutExercises().get(0).getWorkoutExerciseSets().get(1).getId(), WorkoutExerciseSetType.BACKOFF_SET, false, 3, RandomHelper.getRandomBigDecimal(BigDecimal.valueOf(5), BigDecimal.valueOf(150), 2), null, RandomHelper.getRandomInt(1, 8), null, null, null, 150L)
        ));

        List<WorkoutExerciseInputRequest> workoutExerciseInputRequests = new ArrayList<>(List.of(
                WorkoutExerciseHelper.createInputRequest(null, exercises.get(3).getId(), 1, workoutExerciseSetInputRequests1),
                WorkoutExerciseHelper.createInputRequest(workout.getWorkoutExercises().get(0).getId(), exercises.get(1).getId(), 2, workoutExerciseSetInputRequests2)
        ));

        WorkoutInputRequest workoutInputRequest = WorkoutHelper.createInputRequest(newTraineeProfile.getId(), isWorkoutTemplate, tagCreateRequests, workoutExerciseInputRequests);

        WorkoutDetailResponse response = performPut(
                BASE_URL + "/" + workout.getId(),
                workoutInputRequest,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        workout = workoutRepository.getByIdOrThrow(response.id());
        WorkoutHelper.assertInputToEntity(workout, workoutInputRequest);
        WorkoutHelper.assertWorkoutDetailResponse(workout, response);
    }

}