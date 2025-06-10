package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.helper.ProfileHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseHelper;
import sk.krizan.fitness_app_be.helper.WorkoutExerciseSetHelper;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseSetRepository;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@Transactional
@SpringBootTest
class WorkoutExerciseControllerTest {

    @Autowired
    private WorkoutExerciseSetRepository workoutExerciseSetRepository;

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
    void filterWorkoutExercises() {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS)));

        Workout workout1 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WorkoutExercise workoutExercise1 = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout1, exercise, 1));

        Workout workout2 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WorkoutExercise workoutExercise2 = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout2, exercise, 1));

        List<WorkoutExercise> originalList = List.of(workoutExercise1, workoutExercise2);

        WorkoutExerciseFilterRequest request = WorkoutExerciseHelper.createFilterRequest(0, originalList.size(), WorkoutExercise.Fields.id, Sort.Direction.DESC.name(), workout2.getId());

        PageResponse<WorkoutExerciseResponse> response = workoutExerciseController.filterWorkoutExercises(request);

        List<WorkoutExercise> expectedList = new ArrayList<>(List.of(originalList.get(1)));
        WorkoutExerciseHelper.assertFilter(response, expectedList);
    }

    @Test
    void getWorkoutExerciseById() {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS)));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));
        WorkoutExerciseSet workoutExerciseSet = workoutExerciseSetRepository.save(WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 1));
        List<WorkoutExerciseSet> expectedWorkoutExerciseSetList = new ArrayList<>(List.of(workoutExerciseSet));

        WorkoutExerciseResponse response = workoutExerciseController.getWorkoutExerciseById(workoutExercise.getId());
        WorkoutExerciseHelper.assertGet(workoutExercise, response, expectedWorkoutExerciseSetList);
    }

    private static Stream<Arguments> createWorkoutExerciseMethodSource() {
        return Stream.of(
                // Case 1: Create with position 2 -> should insert into position 2, shift WorkoutExercise2 and WorkoutExercise3 to 3 and 4
                Arguments.of(2, 2, List.of(1, 2, 3, 4)),
                // Case 2: Create with position 5 -> beyond current size -> goes to last (4th place)
                Arguments.of(5, 4, List.of(1, 2, 3, 4))
        );
    }

    @ParameterizedTest
    @MethodSource("createWorkoutExerciseMethodSource")
    void createWorkoutExercise(Integer requestedOrder, Integer expectedInsertedOrder, List<Integer> expectedFinalOrder) {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of()));
        List<WorkoutExercise> originalList = List.of(
                WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1),
                WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 2),
                WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 3)
        );
        workoutExerciseRepository.saveAll(originalList);

        WorkoutExerciseCreateRequest request = WorkoutExerciseHelper.createCreateRequest(workout.getId(), exercise.getId(), requestedOrder);
        WorkoutExerciseResponse response = workoutExerciseController.createWorkoutExercise(request);

        List<WorkoutExercise> finalWorkoutExerciseList = workoutExerciseRepository.findAllByWorkoutIdOrderByOrder(workout.getId());
        WorkoutExerciseHelper.assertCreate(request, response, expectedInsertedOrder, finalWorkoutExerciseList, expectedFinalOrder);
    }

    public static Stream<Arguments> updateWorkoutExerciseMethodSource() {
        return Stream.of(
                // Move WorkoutExercise at position 3 to position 1
                Arguments.of(List.of(1, 2, 3, 4), 2, 1, List.of(2, 0, 1, 3)),
                // Move WorkoutExercise at position 1 to position 3
                Arguments.of(List.of(1, 2, 3, 4), 0, 3, List.of(1, 2, 0, 3)),
                // Move WorkoutExercise at position 4 to position 2
                Arguments.of(List.of(1, 2, 3, 4), 3, 2, List.of(0, 3, 1, 2)),
                // Move WorkoutExercise at position 2 to position 4
                Arguments.of(List.of(1, 2, 3, 4), 1, 4, List.of(0, 2, 3, 1)),
                // Move WorkoutExercise at position 2 to the same position (should result in no reordering)
                Arguments.of(List.of(1, 2, 3), 1, 2, List.of(0, 1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("updateWorkoutExerciseMethodSource")
    void updateWorkoutExercise(List<Integer> originalOrders, Integer idOfElementToUpdate, Integer newRequestedOrder, List<Integer> listIdsOfExpectedElementsInOrder) {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of()));

        List<WorkoutExercise> originalList = new ArrayList<>();
        for (Integer order : originalOrders) {
            WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, order);
            originalList.add(workoutExerciseRepository.save(workoutExercise));
        }
        Assertions.assertEquals(originalList.size(), listIdsOfExpectedElementsInOrder.size());
        List<Long> idsOfExpectedElementsInOrder = listIdsOfExpectedElementsInOrder.stream()
                .map(index -> originalList.get(index).getId())
                .collect(Collectors.toList());

        WorkoutExercise workoutExerciseToUpdate = originalList.get(idOfElementToUpdate);
        WorkoutExerciseUpdateRequest request = WorkoutExerciseHelper.createUpdateRequest(workoutExerciseToUpdate.getId(), newRequestedOrder);
        WorkoutExerciseResponse response = workoutExerciseController.updateWorkoutExercise(request);

        List<WorkoutExercise> finalWorkoutExerciseList = workoutExerciseRepository.findAllByWorkoutIdOrderByOrder(workout.getId());
        WorkoutExerciseHelper.assertUpdate(request, response, idsOfExpectedElementsInOrder, finalWorkoutExerciseList);
    }

    @Test
    void deleteWorkoutExercise() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);
        WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1);
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

        Long deletedWorkoutExerciseId = workoutExerciseController.deleteWorkoutExercise(workoutExercise.getId());
        boolean exists = workoutExerciseRepository.existsById(deletedWorkoutExerciseId);

        WorkoutExerciseHelper.assertDelete(exists, workoutExercise, deletedWorkoutExerciseId);
    }


    @Test
    void batchUpdateWorkoutExercises() {
        Workout workout = WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE);
        workout = workoutRepository.save(workout);
        Exercise exercise = ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS));
        exercise = exerciseRepository.save(exercise);

        List<WorkoutExercise> originalList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            WorkoutExercise workoutExercise = WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, i);
            originalList.add(workoutExerciseRepository.save(workoutExercise));
        }

        List<WorkoutExerciseUpdateRequest> requestList = new ArrayList<>();
        for (int i = originalList.size(); i > 0; i--) {
            Long id = originalList.get(originalList.size() - i).getId();
            requestList.add(WorkoutExerciseHelper.createUpdateRequest(id, i));
        }
        BatchUpdateRequest<WorkoutExerciseUpdateRequest> batchRequest = BatchUpdateRequest.<WorkoutExerciseUpdateRequest>builder()
                .updateRequestList(requestList)
                .build();

        SimpleListResponse<WorkoutExerciseResponse> listResponse = workoutExerciseController.batchUpdateWorkoutExercises(batchRequest);

        WorkoutExerciseHelper.assertBatchUpdate(requestList, listResponse);
    }
}