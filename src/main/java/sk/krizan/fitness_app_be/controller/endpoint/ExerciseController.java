package sk.krizan.fitness_app_be.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.service.api.ExerciseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;
}
