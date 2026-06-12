package sk.krizan.fitness_app_be.domain.exercise_muscle_role.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRole;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRoleType;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.Muscle;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.request.ExerciseMuscleRoleInputRequest;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.rest.dto.response.ExerciseMuscleRoleResponse;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseMuscleRoleHelper {

    public static ExerciseMuscleRole createExerciseMuscleRole(Muscle muscle, ExerciseMuscleRoleType type) {
        ExerciseMuscleRole exerciseMuscleRole = new ExerciseMuscleRole();
        exerciseMuscleRole.setMuscle(muscle);
        exerciseMuscleRole.setType(type);

        return exerciseMuscleRole;
    }

    public static ExerciseMuscleRoleInputRequest createInputRequest(Long id, Muscle muscle, ExerciseMuscleRoleType type) {
        return ExerciseMuscleRoleInputRequest.builder()
                .id(id)
                .muscle(muscle)
                .type(type)
                .build();
    }

    public static void assertExerciseMuscleRoleResponse(ExerciseMuscleRole exerciseMuscleRole, ExerciseMuscleRoleResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(exerciseMuscleRole.getId(), response.id());
        Assertions.assertEquals(exerciseMuscleRole.getExercise().getId(), response.exerciseId());
        ReferenceDataHelper.assertReferenceDataResponse(exerciseMuscleRole.getMuscle(), response.muscle());
        ReferenceDataHelper.assertReferenceDataResponse(exerciseMuscleRole.getType(), response.type());
    }

    public static void assertInputRequest(ExerciseMuscleRole exerciseMuscleRole, ExerciseMuscleRoleInputRequest request) {
        Assertions.assertNotNull(exerciseMuscleRole);

        if (request.id() != null) {
            Assertions.assertEquals(exerciseMuscleRole.getId(), request.id());
        } else {
            Assertions.assertNotNull(exerciseMuscleRole.getId());
        }

        Assertions.assertEquals(exerciseMuscleRole.getMuscle(), request.muscle());
        Assertions.assertEquals(exerciseMuscleRole.getType(), request.type());
    }

}
