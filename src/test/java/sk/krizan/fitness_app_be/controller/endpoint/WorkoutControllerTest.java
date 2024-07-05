package sk.krizan.fitness_app_be.controller.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import sk.krizan.fitness_app_be.controller.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.helper.WorkoutHelper;

@SpringBootTest
class WorkoutControllerTest {

    @Autowired
    private WorkoutController workoutController;

    @BeforeEach
    void setUp() {

    }

    @Test
    void filterWorkouts() {
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getWorkoutById() {
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createWorkout() {
        WorkoutDetailResponse workout = workoutController.createWorkout(WorkoutHelper.createCreateRequest());
        WorkoutHelper.assertWorkoutDetailResponse(workout);
    }

    @Test
    void updateWorkout() {
    }

    @Test
    void deleteWorkout() {
    }
}