package sk.krizan.fitness_app_be.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("workout-exercises")
public class WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;
}
