package sk.krizan.fitness_app_be.domain.exercise.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseResponse;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseMapper {

    public static ExerciseResponse entityToResponse(Exercise exercise) {
        return ExerciseResponse.builder()
            .id(exercise.getId())
            .name(exercise.getName())
            .muscleGroupResponseList(
                exercise.getMuscleGroupSet().stream()
                    .map(ReferenceDataMapper::enumToResponse)
                    .collect(Collectors.toList()))
            .build();
    }

    public static Exercise createRequestToEntity(ExerciseCreateRequest request) {
        Exercise exercise = new Exercise();
        exercise.setName(request.name());
        exercise.addToMuscleGroupSet(request.muscleGroupSet());
        return exercise;
    }
}
