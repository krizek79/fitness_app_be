package sk.krizan.fitness_app_be.domain.exercise.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.javafaker.Faker;
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
import sk.krizan.fitness_app_be.common.util.DefaultValues;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.equipment.entity.Equipment;
import sk.krizan.fitness_app_be.domain.equipment.helper.EquipmentHelper;
import sk.krizan.fitness_app_be.domain.equipment.repository.EquipmentRepository;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.entity.ExerciseCategory;
import sk.krizan.fitness_app_be.domain.exercise.entity.MovementPattern;
import sk.krizan.fitness_app_be.domain.exercise.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseDetailResponse;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseSimpleResponse;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRoleType;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.helper.ExerciseMuscleRoleHelper;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;
import sk.krizan.fitness_app_be.domain.media.MediaService;
import sk.krizan.fitness_app_be.domain.media.helper.MediaHelper;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExerciseIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @MockBean
    private MediaService mediaService;

    private static final String BASE_URL = "/exercises";
    private static final Faker faker = new Faker();

    private List<Equipment> equipment;

    @BeforeEach
    void setUp() {
        equipment = equipmentRepository.saveAll(List.of(
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment(),
                EquipmentHelper.createEquipment()
        ));

        when(mediaService.uploadFile(any(), any())).thenReturn(faker.internet().image());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterExercises() throws Exception {
        List<Exercise> exercises = exerciseRepository.saveAll(ExerciseHelper.createOriginalExercises(equipment));

        filterByTitle(exercises);
        filterByExerciseCategory(exercises);
        filterByMovementPatterns(exercises);
        filterByMuscles(exercises);
        filterByRequiredEquipment(exercises);
    }

    private void filterByTitle(List<Exercise> exercises) throws Exception {
        List<Exercise> expectedExerciseList = List.of(exercises.get(0));
        ExerciseFilterRequest request = ExerciseHelper.createFilterRequest(
                0,
                10,
                Exercise.Fields.id,
                Sort.Direction.DESC.name(),
                expectedExerciseList.get(0).getTitle(),
                null,
                null,
                null,
                null
        );
        filter(expectedExerciseList, request);
    }

    private void filterByExerciseCategory(List<Exercise> exercises) throws Exception {
        List<Exercise> expectedExerciseList = List.of(exercises.get(5));
        ExerciseFilterRequest request = ExerciseHelper.createFilterRequest(
                0,
                10,
                Exercise.Fields.id,
                Sort.Direction.DESC.name(),
                null,
                ExerciseCategory.CARDIO,
                null,
                null,
                null
        );
        filter(expectedExerciseList, request);
    }

    private void filterByMovementPatterns(List<Exercise> exercises) throws Exception {
        List<Exercise> expectedExerciseList = List.of(exercises.get(2));
        ExerciseFilterRequest request = ExerciseHelper.createFilterRequest(
                0,
                10,
                Exercise.Fields.id,
                Sort.Direction.DESC.name(),
                null,
                null,
                List.of(MovementPattern.VERTICAL_PULL),
                null,
                null
        );
        filter(expectedExerciseList, request);
    }

    private void filterByMuscles(List<Exercise> exercises) throws Exception {
        List<Exercise> expectedExerciseList = List.of(exercises.get(0), exercises.get(1));

        ExerciseFilterRequest request = ExerciseHelper.createFilterRequest(
                0,
                10,
                Exercise.Fields.id,
                Sort.Direction.DESC.name(),
                null,
                null,
                null,
                List.of(Muscle.CHEST),
                null
        );
        filter(expectedExerciseList, request);
    }

    private void filterByRequiredEquipment(List<Exercise> exercises) throws Exception {
        List<Exercise> expectedExerciseList = List.of(exercises.get(0), exercises.get(1), exercises.get(2), exercises.get(3), exercises.get(4));
        List<Long> equipmentIds = List.of(expectedExerciseList.get(0).getRequiredEquipment().iterator().next().getId());
        ExerciseFilterRequest request = ExerciseHelper.createFilterRequest(
                0,
                10,
                Exercise.Fields.id,
                Sort.Direction.DESC.name(),
                null,
                null,
                null,
                null,
                equipmentIds
        );
        filter(expectedExerciseList, request);
    }

    void filter(List<Exercise> expectedExerciseList, ExerciseFilterRequest request) throws Exception {
        PageResponse<ExerciseSimpleResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                expectedExerciseList,
                response,
                Exercise::getId,
                ExerciseSimpleResponse::id,
                ExerciseHelper::assertExerciseSimpleResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getExerciseById() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createExercise("Bench press", ExerciseCategory.STRENGTH, List.of(MovementPattern.HORIZONTAL_PUSH), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.CHEST, ExerciseMuscleRoleType.PRIMARY)), equipment));

        ExerciseDetailResponse response = performGet(
                BASE_URL + "/" + exercise.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        ExerciseHelper.assertExerciseDetailResponse(exercise, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createExercise() throws Exception {
        List<ExerciseMuscleRoleInputRequest> muscles = List.of(
                ExerciseMuscleRoleHelper.createInputRequest(null, Muscle.CHEST, ExerciseMuscleRoleType.PRIMARY),
                ExerciseMuscleRoleHelper.createInputRequest(null, Muscle.SHOULDERS, ExerciseMuscleRoleType.SECONDARY)
        );

        ExerciseInputRequest request = ExerciseHelper.createInputRequest("Bench press", ExerciseCategory.STRENGTH, Set.of(MovementPattern.HORIZONTAL_PUSH), muscles, List.of(equipment.get(0).getId(), equipment.get(1).getId()));

        ExerciseDetailResponse response = performMultipartPost(
                BASE_URL,
                request,
                MediaHelper.createMockImage("image/jpeg", 1024),
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Exercise exercise = exerciseRepository.getByIdOrThrow(response.id());

        ExerciseHelper.assertInputToEntity(request, exercise);
        ExerciseHelper.assertExerciseDetailResponse(exercise, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateExercise() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createExercise(DefaultValues.DEFAULT_VALUE, ExerciseCategory.MOBILITY, List.of(MovementPattern.HORIZONTAL_PUSH), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.CHEST, ExerciseMuscleRoleType.PRIMARY)), equipment.subList(0, 1)));

        List<ExerciseMuscleRoleInputRequest> muscleInputs = List.of(
                ExerciseMuscleRoleHelper.createInputRequest(exercise.getMuscles().stream().findFirst().orElseThrow().getId(), Muscle.SHOULDERS, ExerciseMuscleRoleType.PRIMARY),
                ExerciseMuscleRoleHelper.createInputRequest(null, Muscle.FOREARMS, ExerciseMuscleRoleType.SECONDARY)
        );

        ExerciseInputRequest request = ExerciseHelper.createInputRequest(DefaultValues.DEFAULT_UPDATE_VALUE, ExerciseCategory.STRENGTH, Set.of(MovementPattern.VERTICAL_PUSH), muscleInputs, List.of(equipment.get(2).getId(), equipment.get(3).getId()));

        ExerciseDetailResponse response = performMultipartPut(
                BASE_URL + "/" + exercise.getId(),
                request,
                MediaHelper.createMockImage("image/jpeg", 1024),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        exercise = exerciseRepository.getByIdOrThrow(response.id());

        ExerciseHelper.assertInputToEntity(request, exercise);
        ExerciseHelper.assertExerciseDetailResponse(exercise, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteExercise() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createExercise("Bench press", ExerciseCategory.STRENGTH, List.of(MovementPattern.HORIZONTAL_PUSH), List.of(ExerciseMuscleRoleHelper.createExerciseMuscleRole(Muscle.CHEST, ExerciseMuscleRoleType.PRIMARY)), equipment));

        performDeleteNoResponse(BASE_URL + "/" + exercise.getId(), HttpStatus.NO_CONTENT);

        exercise = exerciseRepository.findById(exercise.getId()).orElseThrow();

        Assertions.assertTrue(exercise.isDeleted());
    }

}
