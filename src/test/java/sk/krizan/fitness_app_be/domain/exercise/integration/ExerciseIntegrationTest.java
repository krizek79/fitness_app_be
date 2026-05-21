package sk.krizan.fitness_app_be.domain.exercise.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.common.BaseIntegrationTest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.util.DefaultValues;
import sk.krizan.fitness_app_be.common.util.FilterAssertionUtils;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.entity.MuscleGroup;
import sk.krizan.fitness_app_be.domain.exercise.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.domain.exercise.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExerciseIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    private static final String BASE_URL = "/exercises";

    static Stream<Arguments> filterExercisesMethodSource() {
        List<Exercise> originalExerciseList = ExerciseHelper.createOriginalExercises();

        List<Exercise> exerciseListByName = originalExerciseList.stream()
                .filter(exercise -> exercise.getName().equals("Bench press"))
                .collect(Collectors.toList());

        List<MuscleGroup> muscleGroupList = List.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS);
        List<Exercise> exerciseListByMuscleGroupList = originalExerciseList.stream()
                .filter(exercise -> exercise.getMuscleGroups().containsAll(muscleGroupList))
                .collect(Collectors.toList());

        return Stream.of(
                Arguments.of(
                        originalExerciseList,
                        ExerciseHelper.createFilterRequest(0, originalExerciseList.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), null, null)),
                Arguments.of(
                        exerciseListByName,
                        ExerciseHelper.createFilterRequest(0, exerciseListByName.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), "Bench press", null)),
                Arguments.of(
                        exerciseListByMuscleGroupList,
                        ExerciseHelper.createFilterRequest(0, exerciseListByMuscleGroupList.size(), Exercise.Fields.id, Sort.Direction.DESC.name(), null, muscleGroupList))
        );
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("filterExercisesMethodSource")
    void filterExercises(
            List<Exercise> expectedExerciseList,
            ExerciseFilterRequest request
    ) throws Exception {
        List<Exercise> savedExpectedExerciseList = exerciseRepository.saveAll(expectedExerciseList);

        PageResponse<ExerciseResponse> response = performPost(
                BASE_URL + "/filter",
                request,
                new TypeReference<>() {
                },
                HttpStatus.OK);

        FilterAssertionUtils.assertFilterResults(
                savedExpectedExerciseList,
                response,
                Exercise::getId,
                ExerciseResponse::id,
                ExerciseHelper::assertExerciseResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getExerciseById() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS)));

        ExerciseResponse response = performGet(
                BASE_URL + "/" + exercise.getId(),
                new TypeReference<>() {
                },
                HttpStatus.OK);

        ExerciseHelper.assertExerciseResponse(exercise, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createExercise() throws Exception {
        ExerciseCreateRequest request = ExerciseHelper.createCreateRequest(DefaultValues.DEFAULT_VALUE, Set.of(MuscleGroup.FULL_BODY));

        ExerciseResponse response = performPost(
                BASE_URL,
                request,
                new TypeReference<>() {
                },
                HttpStatus.CREATED);

        ExerciseHelper.assertExerciseResponse_create(request, response);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteExercise() throws Exception {
        Exercise exercise = exerciseRepository.save(ExerciseHelper.createExercise("Bench press", Set.of(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS)));

        performDeleteNoResponse(BASE_URL + "/" + exercise.getId(), HttpStatus.NO_CONTENT);

        boolean exists = exerciseRepository.existsById(exercise.getId());

        Assertions.assertFalse(exists);
    }

}
