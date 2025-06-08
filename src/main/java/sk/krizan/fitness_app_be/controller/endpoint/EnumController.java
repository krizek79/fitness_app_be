package sk.krizan.fitness_app_be.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.service.api.EnumService;

import java.util.List;

@RestController
@RequestMapping("enums")
@RequiredArgsConstructor
public class EnumController {

    private final EnumService enumService;

    @GetMapping("workout-levels")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<EnumResponse> getWorkoutLevels() {
        return enumService.getWorkoutLevels();
    }

    @GetMapping("muscle-groups")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<EnumResponse> getMuscleGroups() {
        return enumService.getMuscleGroups();
    }

    @GetMapping("weight-units")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<EnumResponse> getWeightUnits() {
        return enumService.getWeightUnits();
    }

    @GetMapping("workout-exercise-types")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<EnumResponse> getWorkoutExerciseTypes() {
        return enumService.getWorkoutExerciseTypes();
    }

    @GetMapping("workout-exercise-set-types")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<EnumResponse> getWorkoutExerciseSetTypes() {
        return enumService.getWorkoutExerciseSetTypes();
    }
}
