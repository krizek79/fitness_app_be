package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;
import sk.krizan.fitness_app_be.model.mapper.CycleMapper;
import sk.krizan.fitness_app_be.model.mapper.WeekWorkoutMapper;
import sk.krizan.fitness_app_be.service.api.CloneService;

@RestController
@RequiredArgsConstructor
@RequestMapping("clone")
public class CloneController {

    private final CloneService cloneService;

    @PostMapping("cycle/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CycleResponse cloneCycle(@PathVariable Long id) {
        return CycleMapper.entityToResponse(cloneService.cloneCycle(id));
    }

    @PostMapping("workoutToWeekWorkout")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public WeekWorkoutResponse cloneWorkoutToWeekWorkout(@Valid @RequestBody WeekWorkoutCreateRequest request) {
        return WeekWorkoutMapper.entityToResponse(cloneService.cloneWorkoutToWeekWorkout(request));
    }
}
