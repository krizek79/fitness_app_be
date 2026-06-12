package sk.krizan.fitness_app_be.domain.exercise_muscle_role.service.api;

import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;

public interface ExerciseMuscleRoleService {

    void createOrUpdateExerciseMuscleRole(Exercise exercise, ExerciseMuscleRoleInputRequest request);

}
