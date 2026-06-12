package sk.krizan.fitness_app_be.domain.exercise.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.equipment.mapper.EquipmentMapper;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseDetailResponse;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseSimpleResponse;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.entity.ExerciseMuscleRoleType;
import sk.krizan.fitness_app_be.domain.exercise_muscle_role.mapper.ExerciseMuscleRoleMapper;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseMapper {

    public static ExerciseDetailResponse entityToDetailResponse(Exercise exercise) {
        return ExerciseDetailResponse.builder()
                .id(exercise.getId())
                .title(exercise.getTitle())
                .thumbnailUrl(exercise.getThumbnailUrl())
                .exerciseCategory(ReferenceDataMapper.enumToResponse(exercise.getExerciseCategory()))
                .muscles(exercise.getMuscles().stream()
                        .map(ExerciseMuscleRoleMapper::entityToResponse)
                        .collect(Collectors.toList()))
                .movementPatterns(exercise.getMovementPatterns().stream()
                        .map(ReferenceDataMapper::enumToResponse)
                        .collect(Collectors.toList()))
                .requiredEquipment(exercise.getRequiredEquipment().stream()
                        .map(EquipmentMapper::entityToResponse)
                        .toList())
                .build();
    }

    public static ExerciseSimpleResponse entityToSimpleResponse(Exercise exercise) {
        return ExerciseSimpleResponse.builder()
                .id(exercise.getId())
                .title(exercise.getTitle())
                .thumbnailUrl(exercise.getThumbnailUrl())
                .exerciseCategory(ReferenceDataMapper.enumToResponse(exercise.getExerciseCategory()))
                .primaryMuscles(exercise.getMuscles().stream()
                        .filter(exerciseMuscleRole -> ExerciseMuscleRoleType.PRIMARY.equals(exerciseMuscleRole.getType()))
                        .map(exerciseMuscleRole -> ReferenceDataMapper.enumToResponse(exerciseMuscleRole.getMuscle()))
                        .collect(Collectors.toList()))
                .build();
    }

    public static Exercise inputRequestToEntity(Exercise exercise, ExerciseInputRequest request) {
        if (exercise == null) {
            exercise = new Exercise();
        }

        exercise.setTitle(request.title());
        exercise.setExerciseCategory(request.exerciseCategory());

        exercise.getMovementPatterns().clear();
        exercise.addToMovementPatterns(request.movementPatterns());

        return exercise;
    }

}
