package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.service.api.EnumService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnumController implements sk.krizan.fitness_app_be.controller.endpoint.api.EnumController {

    private final EnumService enumService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public List<EnumResponse> getWorkoutLevels() {
        return enumService.getWorkoutLevels();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public List<EnumResponse> getMuscleGroups() {
        return enumService.getMuscleGroups();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public List<EnumResponse> getWeightUnits() {
        return enumService.getWeightUnits();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public List<EnumResponse> getWorkoutExerciseTypes() {
        return enumService.getWorkoutExerciseTypes();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public List<EnumResponse> getWorkoutExerciseSetTypes() {
        return enumService.getWorkoutExerciseSetTypes();
    }
}
