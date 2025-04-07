package sk.krizan.fitness_app_be.model.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseMapper {

    public static ExerciseResponse entityToResponse(Exercise exercise) {
        return ExerciseResponse.builder()
            .id(exercise.getId())
            .name(exercise.getName())
            .muscleGroupValues(
                exercise.getMuscleGroupSet().stream()
                    .map(MuscleGroup::getValue)
                    .collect(Collectors.toList()))
            .build();
    }

    public static Exercise createRequestToEntity(
        ExerciseCreateRequest request,
        Set<MuscleGroup> muscleGroups
    ) {
        Exercise exercise = new Exercise();
        exercise.setName(request.name());
        exercise.addToMuscleGroupSet(muscleGroups);
        return exercise;
    }
}
