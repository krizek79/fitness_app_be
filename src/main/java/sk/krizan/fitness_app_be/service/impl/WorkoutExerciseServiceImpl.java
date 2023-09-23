package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.CreateWorkoutExerciseRequest;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutExerciseService;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final UserService userService;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Override
    public WorkoutExercise getWorkoutExerciseById(Long id) {
        return workoutExerciseRepository.findById(id).orElseThrow(
            () -> new NotFoundException("WorkoutExercise with id { " + id + " } does not exist."));
    }

    @Override
    public WorkoutExercise createWorkoutExcercise(CreateWorkoutExerciseRequest request) {
        return null;
    }

    @Override
    public Long deleteWorkoutExercise(Long id) {
        User currentUser = userService.getCurrentUser();
        WorkoutExercise workoutExercise = getWorkoutExerciseById(id);

        if (workoutExercise.getWorkout().getAuthor().getUser() != currentUser
            && !currentUser.getRoles().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        workoutExerciseRepository.delete(workoutExercise);
        return workoutExercise.getId();
    }
}
