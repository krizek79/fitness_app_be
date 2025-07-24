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
import sk.krizan.fitness_app_be.controller.endpoint.api.WorkoutExerciseSetController;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;
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
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;
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
class WorkoutExerciseSetControllerTest {

    @Autowired
    private WorkoutExerciseSetController workoutExerciseSetController;

    @Autowired
    private WorkoutExerciseSetRepository workoutExerciseSetRepository;

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
        User mockUser = UserHelper.createMockUser(Set.of(Role.ADMIN));
        mockUser = userRepository.save(mockUser);

        mockProfile = ProfileHelper.createMockProfile(mockUser);
        mockProfile = profileRepository.save(mockProfile);
        mockUser.setProfile(mockProfile);

        SecurityHelper.setAuthentication(mockUser);
        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void filterWorkoutExerciseSets() {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS)));

        Workout workout1 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WorkoutExercise workoutExercise1 = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout1, exercise, 1));
        WorkoutExerciseSet workoutExerciseSet1 = workoutExerciseSetRepository.save(WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise1, 1));

        Workout workout2 = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), DEFAULT_VALUE));
        WorkoutExercise workoutExercise2 = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout2, exercise, 1));
        WorkoutExerciseSet workoutExerciseSet2 = workoutExerciseSetRepository.save(WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise2, 1));

        List<WorkoutExerciseSet> originalList = List.of(workoutExerciseSet1, workoutExerciseSet2);

        WorkoutExerciseSetFilterRequest request = WorkoutExerciseSetHelper.createFilterRequest(0, originalList.size(), WorkoutExercise.Fields.id, Sort.Direction.DESC.name(), workoutExercise2.getId());

        PageResponse<WorkoutExerciseSetResponse> response = workoutExerciseSetController.filterWorkoutExerciseSets(request);

        List<WorkoutExerciseSet> expectedList = new ArrayList<>(List.of(originalList.get(1)));
        WorkoutExerciseSetHelper.assertFilter(response, expectedList);
    }

    @Test
    void getWorkoutExerciseSetById() {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.FULL_BODY)));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));
        WorkoutExerciseSet workoutExerciseSet = workoutExerciseSetRepository.save(WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 1));

        WorkoutExerciseSetResponse response = workoutExerciseSetController.getWorkoutExerciseSetById(workoutExerciseSet.getId());

        WorkoutExerciseSetHelper.assertGet(workoutExerciseSet, response);
    }

    private static Stream<Arguments> createWorkoutExerciseSetMethodSource() {
        return Stream.of(
                // Case 1: Create with position 2 -> should insert into position 2, shift WorkoutExerciseSet2 and WorkoutExerciseSet3 to 3 and 4
                Arguments.of(2, 2, List.of(1, 2, 3, 4)),
                // Case 2: Create with position 5 -> beyond current size -> goes to last (4th place)
                Arguments.of(5, 4, List.of(1, 2, 3, 4))
        );
    }

    @ParameterizedTest
    @MethodSource("createWorkoutExerciseSetMethodSource")
    void createWorkoutExerciseSet(Integer requestedOrder, Integer expectedInsertedOrder, List<Integer> expectedFinalOrder) {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.FULL_BODY)));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));
        List<WorkoutExerciseSet> originalList = List.of(
                WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 1),
                WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 2),
                WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 3)
        );
        workoutExerciseSetRepository.saveAll(originalList);

        WorkoutExerciseSetCreateRequest request = WorkoutExerciseSetHelper.createCreateRequest(workoutExercise.getId(), requestedOrder);
        WorkoutExerciseSetResponse response = workoutExerciseSetController.createWorkoutExerciseSet(request);

        List<WorkoutExerciseSet> finalWorkoutExerciseSetList = workoutExerciseSetRepository.findAllByWorkoutExerciseIdOrderByOrder(workoutExercise.getId());
        WorkoutExerciseSetHelper.assertCreate(request, response, expectedInsertedOrder, finalWorkoutExerciseSetList, expectedFinalOrder);
    }

    public static Stream<Arguments> updateWorkoutExerciseSetMethodSource() {
        return Stream.of(
                // Move WorkoutExerciseSet at position 3 to position 1
                Arguments.of(List.of(1, 2, 3, 4), 2, 1, List.of(2, 0, 1, 3)),
                // Move WorkoutExerciseSet at position 1 to position 3
                Arguments.of(List.of(1, 2, 3, 4), 0, 3, List.of(1, 2, 0, 3)),
                // Move WorkoutExerciseSet at position 4 to position 2
                Arguments.of(List.of(1, 2, 3, 4), 3, 2, List.of(0, 3, 1, 2)),
                // Move WorkoutExerciseSet at position 2 to position 4
                Arguments.of(List.of(1, 2, 3, 4), 1, 4, List.of(0, 2, 3, 1)),
                // Move WorkoutExerciseSet at position 2 to the same position (should result in no reordering)
                Arguments.of(List.of(1, 2, 3), 1, 2, List.of(0, 1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("updateWorkoutExerciseSetMethodSource")
    void updateWorkoutExerciseSet(List<Integer> originalOrders, Integer idOfElementToUpdate, Integer newRequestedOrder, List<Integer> listIdsOfExpectedElementsInOrder) {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of()));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));

        List<WorkoutExerciseSet> originalList = new ArrayList<>();
        for (Integer order : originalOrders) {
            WorkoutExerciseSet workoutExerciseSet = WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, order);
            originalList.add(workoutExerciseSetRepository.save(workoutExerciseSet));
        }
        Assertions.assertEquals(originalList.size(), listIdsOfExpectedElementsInOrder.size());
        List<Long> idsOfExpectedElementsInOrder = listIdsOfExpectedElementsInOrder.stream()
                .map(index -> originalList.get(index).getId())
                .collect(Collectors.toList());

        WorkoutExerciseSet workoutExerciseSetToUpdate = originalList.get(idOfElementToUpdate);
        WorkoutExerciseSetUpdateRequest request = WorkoutExerciseSetHelper.createUpdateRequest(workoutExerciseSetToUpdate.getId(), newRequestedOrder, WorkoutExerciseSetType.WARMUP.getKey());
        WorkoutExerciseSetResponse response = workoutExerciseSetController.updateWorkoutExerciseSet(request);

        List<WorkoutExerciseSet> finalWorkoutExerciseSetList = workoutExerciseSetRepository.findAllByWorkoutExerciseIdOrderByOrder(workoutExercise.getId());
        WorkoutExerciseSetHelper.assertUpdate(request, response, idsOfExpectedElementsInOrder, finalWorkoutExerciseSetList);
    }

    @Test
    void batchUpdateWorkoutExerciseSets() {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.FULL_BODY)));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));

        List<WorkoutExerciseSet> originalList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            WorkoutExerciseSet workoutExerciseSet = WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, i);
            originalList.add(workoutExerciseSetRepository.save(workoutExerciseSet));
        }

        List<WorkoutExerciseSetUpdateRequest> requestList = new ArrayList<>();
        for (int i = originalList.size(); i > 0; i--) {
            Long id = originalList.get(originalList.size() - i).getId();
            requestList.add(WorkoutExerciseSetHelper.createUpdateRequest(id, i, WorkoutExerciseSetType.STRAIGHT_SET.getKey()));
        }
        BatchUpdateRequest<WorkoutExerciseSetUpdateRequest> batchRequest = BatchUpdateRequest.<WorkoutExerciseSetUpdateRequest>builder()
                .updateRequestList(requestList)
                .build();

        SimpleListResponse<WorkoutExerciseSetResponse> listResponse = workoutExerciseSetController.batchUpdateWorkoutExerciseSets(batchRequest);

        WorkoutExerciseSetHelper.assertBatchUpdate(requestList, listResponse);
    }

    @Test
    void deleteWorkoutExerciseSet() {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.FULL_BODY)));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));
        WorkoutExerciseSet workoutExerciseSet = workoutExerciseSetRepository.save(WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 1));

        Long deleteWorkoutExerciseSetId = workoutExerciseSetController.deleteWorkoutExerciseSet(workoutExerciseSet.getId());

        boolean exists = workoutExerciseSetRepository.existsById(deleteWorkoutExerciseSetId);
        WorkoutExerciseSetHelper.assertDelete(exists, workoutExerciseSet, deleteWorkoutExerciseSetId);
    }

    @Test
    void triggerCompleted() {
        Workout workout = workoutRepository.save(WorkoutHelper.createMockWorkout(mockProfile, new HashSet<>(), UUID.randomUUID().toString()));
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createMockExercise(UUID.randomUUID().toString(), Set.of(MuscleGroup.FULL_BODY)));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(WorkoutExerciseHelper.createMockWorkoutExercise(workout, exercise, 1));
        WorkoutExerciseSet workoutExerciseSet = workoutExerciseSetRepository.save(WorkoutExerciseSetHelper.createMockWorkoutExerciseSet(workoutExercise, 1));
        Boolean originalState = workoutExerciseSet.getCompleted();

        WorkoutExerciseSetResponse response = workoutExerciseSetController.triggerCompleted(workoutExerciseSet.getId());
        WorkoutExerciseSetHelper.assertTriggerCompleted(originalState, response);
    }
}