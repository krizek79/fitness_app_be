package sk.krizan.fitness_app_be.controller.endpoint;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.model.enums.Level;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;

@RestController
@RequestMapping("enums")
public class EnumsController {

    @GetMapping("workout-levels")
    public List<String> getWorkoutLevels() {
        Level[] levels = Level.values();
        return Arrays.stream(levels)
            .map(Level::getValue)
            .collect(Collectors.toList());
    }

    @GetMapping("muscle-groups")
    public List<String> getMuscleGroups() {
        MuscleGroup[] levels = MuscleGroup.values();
        return Arrays.stream(levels)
            .map(MuscleGroup::getValue)
            .collect(Collectors.toList());
    }
}
