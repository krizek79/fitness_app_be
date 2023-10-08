package sk.krizan.fitness_app_be.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.enums.MuscleGroup;
import sk.krizan.fitness_app_be.model.mapper.ExerciseMapper;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.service.api.EnumService;
import sk.krizan.fitness_app_be.service.api.ExerciseService;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final EnumService enumService;

    private final static String ERROR_EXERCISE_NOT_FOUND =
        "Exercise with workoutId { %s } does not exist.";

    @Override
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(
            () -> new NotFoundException(ERROR_EXERCISE_NOT_FOUND.formatted(id)));
    }

    @Override
    public Exercise createExercise(ExerciseCreateRequest request) {
        Set<MuscleGroup> muscleGroups = request.muscleGroupKeys().stream()
            .map(key -> (MuscleGroup) enumService.findEnumByKey(key))
            .collect(Collectors.toSet());
        Exercise exercise = ExerciseMapper.createRequestToEntity(request, muscleGroups);
        return exerciseRepository.save(exercise);
    }

    @Override
    public Long deleteExercise(Long id) {
        Exercise exercise = getExerciseById(id);
        exerciseRepository.delete(exercise);
        return exercise.getId();
    }
}
