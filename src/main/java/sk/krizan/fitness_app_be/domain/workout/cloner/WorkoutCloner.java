package sk.krizan.fitness_app_be.domain.workout.cloner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.common.cloning.AbstractCloner;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout_exercise.cloner.WorkoutExerciseCloner;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkoutCloner extends AbstractCloner<Workout> {

    private final UserService userService;

    private final WorkoutExerciseCloner workoutExerciseCloner;

    @Override
    public Class<Workout> getHandledClass() {
        return Workout.class;
    }

    @Override
    public Workout clone(Workout original) {
        Workout clone = new Workout();
        clone.setTitle(original.getTitle());
        clone.setDescription(original.getDescription());
        clone.addToTags(original.getTags());
        clone.setWeightUnit(original.getWeightUnit());
        clone.setDistanceUnit(original.getDistanceUnit());

        List<WorkoutExercise> clonedWorkoutExercises = original.getWorkoutExercises().stream()
                .map(workoutExerciseCloner::clone)
                .toList();
        clonedWorkoutExercises.forEach(clone::addToWorkoutExercises);

        Profile currentUserProfile = userService.getOrCreateCurrentUser().getProfile();
        currentUserProfile.addToAuthoredWorkouts(clone);

        return clone;
    }

}
