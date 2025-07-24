package sk.krizan.fitness_app_be.controller.endpoint;

import org.apache.logging.log4j.util.TriConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.endpoint.api.ExerciseController;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.DefaultValues;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@SpringBootTest
public class ExerciseControllerTest {

    @Autowired
    private ExerciseController exerciseController;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser(Set.of(Role.ADMIN));
        SecurityHelper.setAuthentication(mockUser);
    }

    static Stream<Arguments> filterExercisesMethodSource() {
        List<Exercise> originalExerciseList = ExerciseHelper.createOriginalExercises();

        List<Exercise> exerciseListByName = originalExerciseList.stream()
                .filter(exercise -> exercise.getName().equals("Bench press"))
                .collect(Collectors.toList());

        List<MuscleGroup> muscleGroupList = List.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS);
        List<Exercise> exerciseListByMuscleGroupList = originalExerciseList.stream()
                .filter(exercise -> exercise.getMuscleGroupSet().containsAll(muscleGroupList))
                .collect(Collectors.toList());

        return Stream.of(
                Arguments.of(
                        originalExerciseList,
                        ExerciseHelper.createFilterRequest(0, originalExerciseList.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), null, null),
                        (TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>>) ExerciseHelper::assertFilter),
                Arguments.of(
                        exerciseListByName,
                        ExerciseHelper.createFilterRequest(0, exerciseListByName.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), "Bench press", null),
                        (TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>>) ExerciseHelper::assertFilter),
                Arguments.of(
                        exerciseListByMuscleGroupList,
                        ExerciseHelper.createFilterRequest(0, exerciseListByMuscleGroupList.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), null, muscleGroupList),
                        (TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>>) ExerciseHelper::assertFilter)
        );
    }

    @ParameterizedTest
    @MethodSource("filterExercisesMethodSource")
    void filterExercises(
            List<Exercise> expectedExerciseList,
            ExerciseFilterRequest request,
            TriConsumer<List<Exercise>, ExerciseFilterRequest, PageResponse<ExerciseResponse>> assertion
    ) {
        List<Exercise> savedExpectedExerciseList = exerciseRepository.saveAll(expectedExerciseList);
        PageResponse<ExerciseResponse> response = exerciseController.filterExercises(request);
        assertion.accept(savedExpectedExerciseList, request, response);
    }

    @Test
    void getExerciseById() {
        Exercise exercise = ExerciseHelper.createMockExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        exercise = exerciseRepository.save(exercise);

        ExerciseResponse response = exerciseController.getExerciseById(exercise.getId());

        ExerciseHelper.assertExerciseResponse_get(exercise, response);
    }

    @Test
    void createExercise() {
        ExerciseCreateRequest request = ExerciseHelper.createCreateRequest(
                DefaultValues.DEFAULT_VALUE,
                Set.of(MuscleGroup.FULL_BODY.name()));
        ExerciseResponse response = exerciseController.createExercise(request);
        ExerciseHelper.assertExerciseResponse_create(request, response);
    }

    @Test
    void deleteExercise() {
        Exercise exercise = ExerciseHelper.createMockExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS));
        exercise = exerciseRepository.save(exercise);

        Long deletedExerciseId = exerciseController.deleteExercise(exercise.getId());
        boolean exists = exerciseRepository.existsById(deletedExerciseId);

        ExerciseHelper.assertDelete(exists, exercise, deletedExerciseId);
    }
}
