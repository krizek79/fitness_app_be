package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.service.api.ExerciseService;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    private final WorkoutExerciseRepository workoutExerciseRepository;

    private static final String ERROR_NOT_FOUND = "WorkoutExercise with id { %s } does not exist.";

    @Override
    public WorkoutExercise getWorkoutExerciseById(Long id) {
        return workoutExerciseRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public WorkoutExercise createWorkoutExercise(WorkoutExerciseCreateRequest request) {
        Workout workout = workoutService.getWorkoutById(request.workoutId());
        Exercise exercise = exerciseService.getExerciseById(request.exerciseId());
        return workoutExerciseRepository.save(
            WorkoutExerciseMapper.createRequestToEntity(request, workout, exercise));
    }

    @Override
    @Transactional
    public WorkoutExercise updateWorkoutExercise(Long id, WorkoutExerciseUpdateRequest request) {
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);
        return workoutExerciseRepository.save(
            WorkoutExerciseMapper.updateRequestToEntity(workoutExercise, request));
    }

    @Override
    public Long deleteWorkoutExercise(Long id) {
        User currentUser = userService.getCurrentUser();
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);

        if (workoutExercise.getWorkout().getAuthor().getUser() != currentUser
            && !currentUser.getRoleSet().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        workoutExerciseRepository.delete(workoutExercise);
        return workoutExercise.getId();
    }
}
