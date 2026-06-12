package sk.krizan.fitness_app_be.domain.exercise_muscle_role.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRole;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.mapper.ExerciseMuscleRoleMapper;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.service.api.ExerciseMuscleRoleService;

@Service
@RequiredArgsConstructor
public class ExerciseMuscleRoleServiceImpl implements ExerciseMuscleRoleService {

    @Override
    public void createOrUpdateExerciseMuscleRole(Exercise exercise, ExerciseMuscleRoleInputRequest request) {
        ExerciseMuscleRole exerciseMuscleRole;
        if (request.id() != null) {
            //  Update existing exercise muscle role
            exerciseMuscleRole = exercise.getMuscles().stream()
                    .filter(we -> we.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Exercise muscle role with id %d not found in exercise with id %d.".formatted(request.id(), exercise.getId())));
            ExerciseMuscleRoleMapper.inputRequestToEntity(exerciseMuscleRole, request, exercise);
        } else {
            //  Create new exercise muscle role
            ExerciseMuscleRoleMapper.inputRequestToEntity(null, request, exercise);
        }
    }

}
