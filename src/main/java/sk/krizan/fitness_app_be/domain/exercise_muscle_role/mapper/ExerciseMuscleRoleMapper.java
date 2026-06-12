package sk.krizan.fitness_app_be.domain.exercise_muscle_role.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRole;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.response.ExerciseMuscleRoleResponse;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseMuscleRoleMapper {

    public static ExerciseMuscleRoleResponse entityToResponse(ExerciseMuscleRole exerciseMuscleRole) {
        return ExerciseMuscleRoleResponse.builder()
                .id(exerciseMuscleRole.getId())
                .exerciseId(exerciseMuscleRole.getExercise().getId())
                .muscle(ReferenceDataMapper.enumToResponse(exerciseMuscleRole.getMuscle()))
                .type(ReferenceDataMapper.enumToResponse(exerciseMuscleRole.getType()))
                .build();
    }

    public static ExerciseMuscleRole inputRequestToEntity(ExerciseMuscleRole exerciseMuscleRole, ExerciseMuscleRoleInputRequest request, Exercise exercise) {
        if (exerciseMuscleRole == null) {
            exerciseMuscleRole = new ExerciseMuscleRole();
            exercise.addToMuscles(exerciseMuscleRole);
        }

        exerciseMuscleRole.setMuscle(request.muscle());
        exerciseMuscleRole.setType(request.type());

        return exerciseMuscleRole;
    }

}
