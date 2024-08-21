package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.helper.ExerciseHelper;
import sk.krizan.fitness_app_be.helper.SecurityHelper;
import sk.krizan.fitness_app_be.helper.UserHelper;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;

import java.util.List;
import java.util.Set;

@Transactional
@SpringBootTest
public class ExerciseControllerTest {

    @Autowired
    private ExerciseController exerciseController;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private List<Exercise> originalExerciseList;

    @BeforeEach
    void setUp() {
        User mockUser = UserHelper.createMockUser("admin@test.com", Set.of(Role.ADMIN));
        SecurityHelper.setAuthentication(mockUser);

        originalExerciseList = ExerciseHelper.createOriginalExercises();
        originalExerciseList = exerciseRepository.saveAll(originalExerciseList);
    }

    @Test
    void filterExercises() {
        ExerciseFilterRequest request = ExerciseHelper.createFilterRequest(
                0,
                2,
                Exercise.Fields.id,
                Sort.Direction.DESC.name(),
                null,
                null);
        PageResponse<ExerciseResponse> response = exerciseController.filterExercises(request);
        ExerciseHelper.assertFilter(originalExerciseList, request, response);
    }

    @Test
    void getExerciseById() {
    }

    @Test
    void createExercise() {
    }

    @Test
    void deleteExercise() {
        Exercise exercise = originalExerciseList.get(0);

        Long deletedExerciseId = exerciseController.deleteExercise(exercise.getId());
        boolean exists = exerciseRepository.existsById(deletedExerciseId);

        ExerciseHelper.assertDelete(exists, exercise, deletedExerciseId);
    }
}
