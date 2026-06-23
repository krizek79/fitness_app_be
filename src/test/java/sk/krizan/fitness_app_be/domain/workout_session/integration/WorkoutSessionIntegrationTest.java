package sk.krizan.fitness_app_be.domain.workout_session.integration;

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
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
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
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.domain.workout.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.helper.WorkoutExerciseSessionHelper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSetType;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request.WorkoutExerciseSetResultInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.entity.WorkoutSession;
import sk.krizan.fitness_app_be.domain.workout_session.helper.WorkoutSessionHelper;
import sk.krizan.fitness_app_be.domain.workout_session.repository.WorkoutSessionRepository;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionDetailResponse;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionSimpleResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

class WorkoutSessionIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

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

    private static final String BASE_URL = "/workout-sessions";

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
    void filterWorkoutSessions() throws Exception {
        Workout workout1 = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, Set.of(), new ArrayList<>(), "Session Workout 1", false));
        Workout workout2 = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, Set.of(), new ArrayList<>(), "Session Workout 2", false));

        WorkoutSession session1 = workoutSessionRepository.save(WorkoutSessionHelper.createWorkoutSession(workout1, null));
        WorkoutSession session2 = workoutSessionRepository.save(WorkoutSessionHelper.createWorkoutSession(workout1, null));
        WorkoutSession session3 = workoutSessionRepository.save(WorkoutSessionHelper.createWorkoutSession(workout2, null));

        filterWorkoutSessions_ByWorkoutId(List.of(session1, session2), workout1.getId());
        filterWorkoutSessions_ByStatus(List.of(session1, session2, session3), WorkoutStatus.NOT_STARTED);
    }

    private void filterWorkoutSessions_ByWorkoutId(List<WorkoutSession> expectedSessions, Long workoutId) throws Exception {
        WorkoutSessionFilterRequest request = WorkoutSessionHelper.createFilterRequest(
                0, 10, WorkoutSession.Fields.id, Sort.Direction.ASC.name(),
                workoutId, null, null, null);
        PageResponse<WorkoutSessionSimpleResponse> response = performPost(
                BASE_URL + "/filter", request, new TypeReference<>() {}, HttpStatus.OK);
        FilterAssertionUtils.assertFilterResults(expectedSessions, response, WorkoutSession::getId, WorkoutSessionSimpleResponse::id,
                WorkoutSessionHelper::assertWorkoutSessionSimpleResponse);
    }

    private void filterWorkoutSessions_ByStatus(List<WorkoutSession> expectedSessions, WorkoutStatus status) throws Exception {
        WorkoutSessionFilterRequest request = WorkoutSessionHelper.createFilterRequest(
                0, 10, WorkoutSession.Fields.id, Sort.Direction.ASC.name(),
                null, null, null, status);
        PageResponse<WorkoutSessionSimpleResponse> response = performPost(
                BASE_URL + "/filter", request, new TypeReference<>() {}, HttpStatus.OK);
        FilterAssertionUtils.assertFilterResults(expectedSessions, response, WorkoutSession::getId, WorkoutSessionSimpleResponse::id,
                WorkoutSessionHelper::assertWorkoutSessionSimpleResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWorkoutSessionById() throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, Set.of(), new ArrayList<>(), "Get Session Workout", false));
        WorkoutSession workoutSession = workoutSessionRepository.save(WorkoutSessionHelper.createWorkoutSession(workout, null));

        WorkoutSessionDetailResponse response = performGet(
                BASE_URL + "/" + workoutSession.getId(),
                new TypeReference<>() {},
                HttpStatus.OK);

        WorkoutSessionHelper.assertWorkoutSessionDetailResponse(workoutSession, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createWorkoutSession() throws Exception {
        WorkoutExercise workoutExercise = WorkoutExerciseHelper.createWorkoutExercise(exercises.get(0), new ArrayList<>(), 1);
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, Set.of(), List.of(workoutExercise), "Create Session Workout", false));
        WorkoutExercise savedExercise = workout.getWorkoutExercises().get(0);

        List<WorkoutExerciseSetResultInputRequest> setResults = List.of(
                WorkoutExerciseSetResultInputRequest.builder()
                        .workoutExerciseSetType(WorkoutExerciseSetType.STRAIGHT_SET)
                        .order(1)
                        .repetitions(8)
                        .weight(BigDecimal.valueOf(100))
                        .restDurationSeconds(90L)
                        .completed(true)
                        .build()
        );

        List<WorkoutExerciseSessionInputRequest> exerciseSessions = List.of(
                WorkoutExerciseSessionHelper.createInputRequest(null, savedExercise.getId(), 1, null, setResults)
        );

        WorkoutSessionInputRequest request = WorkoutSessionHelper.createInputRequest(
                workout.getId(), null, WorkoutStatus.IN_PROGRESS, exerciseSessions);

        WorkoutSessionDetailResponse response = performPost(
                BASE_URL, request, new TypeReference<>() {}, HttpStatus.CREATED);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        WorkoutSession created = workoutSessionRepository.getByIdOrThrow(response.id());
        WorkoutSessionHelper.assertInputToEntity(created, request);
        WorkoutSessionHelper.assertWorkoutSessionDetailResponse(created, response);
        Assertions.assertEquals(1, response.workoutExerciseSessions().size());
        Assertions.assertEquals(1, response.workoutExerciseSessions().get(0).workoutExerciseSetResults().size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWorkoutSession() throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, Set.of(), new ArrayList<>(), "Update Session Workout", false));
        WorkoutSession workoutSession = workoutSessionRepository.save(WorkoutSessionHelper.createWorkoutSession(workout, null));

        WorkoutSessionInputRequest request = WorkoutSessionHelper.createInputRequest(
                workout.getId(), null, WorkoutStatus.COMPLETED, List.of());

        WorkoutSessionDetailResponse response = performPut(
                BASE_URL + "/" + workoutSession.getId(),
                request,
                new TypeReference<>() {},
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(WorkoutStatus.COMPLETED, response.status());
        WorkoutSession updated = workoutSessionRepository.getByIdOrThrow(workoutSession.getId());
        Assertions.assertEquals(WorkoutStatus.COMPLETED, updated.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWorkoutSession() throws Exception {
        Workout workout = workoutRepository.save(WorkoutHelper.createWorkout(mockProfile, Set.of(), new ArrayList<>(), "Delete Session Workout", false));
        WorkoutSession workoutSession = workoutSessionRepository.save(WorkoutSessionHelper.createWorkoutSession(workout, null));

        performDeleteNoResponse(BASE_URL + "/" + workoutSession.getId(), HttpStatus.NO_CONTENT);

        Assertions.assertFalse(workoutSessionRepository.findById(workoutSession.getId()).isPresent());
    }

}
