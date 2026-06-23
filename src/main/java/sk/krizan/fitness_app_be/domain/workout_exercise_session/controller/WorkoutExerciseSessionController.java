package sk.krizan.fitness_app_be.domain.workout_exercise_session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.mapper.WorkoutExerciseSessionMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.response.WorkoutExerciseSessionResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.service.api.WorkoutExerciseSessionService;

@RestController
@RequiredArgsConstructor
public class WorkoutExerciseSessionController implements sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.api.WorkoutExerciseSessionController {

    private final WorkoutExerciseSessionService workoutExerciseSessionService;

    @Override
    @PreAuthorize("hasPermission(#id, 'WORKOUT_EXERCISE_SESSION', 'READ')")
    public WorkoutExerciseSessionResponse getWorkoutExerciseSessionById(Long id) {
        return WorkoutExerciseSessionMapper.entityToResponse(workoutExerciseSessionService.getWorkoutExerciseSessionById(id));
    }

}
