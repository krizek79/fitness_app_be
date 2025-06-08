package sk.krizan.fitness_app_be.cloning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkoutExerciseCloner extends AbstractCloner<WorkoutExercise> {

    private final WorkoutExerciseSetCloner workoutExerciseSetCloner;

    @Override
    public Class<WorkoutExercise> getHandledClass() {
        return WorkoutExercise.class;
    }

    @Override
    public WorkoutExercise clone(WorkoutExercise original, CloneContext context) {
        if (context.isAlreadyCloned(original)) {
            return context.getCachedClone(original);
        }

        WorkoutExercise clone = new WorkoutExercise();
        clone.setExercise(original.getExercise());
        clone.setOrder(original.getOrder());
        clone.setWorkoutExerciseType(original.getWorkoutExerciseType());
        List<WorkoutExerciseSet> clonedWorkoutExercises = original.getWorkoutExerciseSetList().stream()
                .map(workoutExerciseSet -> workoutExerciseSetCloner.clone(workoutExerciseSet, context))
                .toList();
        clone.addToWorkoutExerciseSetList(clonedWorkoutExercises);

        context.cacheClone(original, clone);

        return clone;
    }
}
