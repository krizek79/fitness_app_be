package sk.krizan.fitness_app_be.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.model.mapper.ExerciseMapper;
import sk.krizan.fitness_app_be.service.api.ExerciseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ExerciseResponse getExerciseByUd(@PathVariable Long id) {
        return ExerciseMapper.entityToResponse(exerciseService.getExerciseById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ExerciseResponse createExercise(@Valid @RequestBody ExerciseCreateRequest request) {
        return ExerciseMapper.entityToResponse(exerciseService.createExercise(request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Long deleteExercise(@PathVariable Long id) {
        return exerciseService.deleteExercise(id);
    }
}
