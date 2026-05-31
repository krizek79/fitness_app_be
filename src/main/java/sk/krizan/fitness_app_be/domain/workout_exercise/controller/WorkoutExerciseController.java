package sk.krizan.fitness_app_be.domain.workout_exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.workout_exercise.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.service.api.WorkoutExerciseService;

@RestController
@RequiredArgsConstructor
public class WorkoutExerciseController implements sk.krizan.fitness_app_be.domain.workout_exercise.rest.api.WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT_EXERCISE', 'READ')")
    public WorkoutExerciseResponse getWorkoutExerciseById(Long id) {
        return WorkoutExerciseMapper.entityToResponse(workoutExerciseService.getWorkoutExerciseById(id));
    }

}
