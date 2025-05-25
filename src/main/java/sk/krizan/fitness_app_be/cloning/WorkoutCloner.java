package sk.krizan.fitness_app_be.cloning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkoutCloner extends AbstractCloner<Workout> {

    private final WorkoutExerciseCloner workoutExerciseCloner;

    @Override
    public Class<Workout> getHandledClass() {
        return Workout.class;
    }

    @Override
    public Workout clone(Workout original, CloneContext context) {
        if (context.isAlreadyCloned(original)) {
            return context.getCachedClone(original);
        }

        Workout clone = new Workout();
        clone.setName(original.getName());
        clone.setDescription(original.getDescription());
        clone.setAuthor(original.getAuthor());
        clone.setTrainee(context.getTargetProfile());
        clone.addToTagSet(original.getTagSet());
        List<WorkoutExercise> clonedWorkoutExercises = original.getWorkoutExerciseList().stream()
                .map(workoutExercise -> workoutExerciseCloner.clone(workoutExercise, context))
                .peek(workoutExercise -> workoutExercise.setWorkout(clone))
                .toList();
        clone.addToWorkoutExerciseList(clonedWorkoutExercises);

        context.cacheClone(original, clone);

        return clone;
    }
}
