package sk.krizan.fitness_app_be.model.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.CreateExerciseRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseMapper {

    public static ExerciseResponse exerciseToResponse(Exercise exercise) {
        return ExerciseResponse.builder()
            .id(exercise.getId())
            .name(exercise.getName())
            .muscleGroupValues(
                exercise.getMuscleGroups().stream()
                    .map(MuscleGroup::getValue)
                    .collect(Collectors.toList()))
            .build();
    }

    public static Exercise createExerciseRequestToExercise(
        CreateExerciseRequest request,
        Set<MuscleGroup> muscleGroups
    ) {
        return Exercise.builder()
            .name(request.name())
            .muscleGroups(muscleGroups)
            .build();
    }
}
