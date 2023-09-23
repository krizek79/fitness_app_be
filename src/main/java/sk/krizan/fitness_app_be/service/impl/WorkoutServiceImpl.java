package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.ForbiddenException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.CreateWorkoutRequest;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.WorkoutRepository;
import sk.krizan.fitness_app_be.service.api.UserService;
import sk.krizan.fitness_app_be.service.api.WorkoutService;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final UserService userService;
    private final WorkoutRepository workoutRepository;

    @Override
    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Workout with id { " + id + " } does not exist."));
    }

    @Override
    public Workout createWorkout(CreateWorkoutRequest request) {
        return null;
    }

    @Override
    public Long deleteWorkout(Long id) {
        User currentUser = userService.getCurrentUser();
        Workout workout = getWorkoutById(id);

        if (workout.getAuthor().getUser() != currentUser
            && !currentUser.getRoles().contains(Role.ADMIN)
        ) {
            throw new ForbiddenException();
        }

        workoutRepository.delete(workout);
        return workout.getId();
    }
}
