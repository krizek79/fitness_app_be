package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.repository.ExerciseRepository;
import sk.krizan.fitness_app_be.service.api.ExerciseService;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Override
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Exercise with id { " + id + " } does not exist."));
    }

    @Override
    public Long deleteExercise(Long id) {
        Exercise exercise = getExerciseById(id);
        exerciseRepository.delete(exercise);
        return exercise.getId();
    }
}
