package sk.krizan.fitness_app_be.cloning;

import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

@Component
public class WorkoutExerciseCloner extends AbstractCloner<WorkoutExercise> {

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
        clone.setSets(original.getSets());
        clone.setRepetitions(original.getRepetitions());
        clone.setRestDuration(original.getRestDuration());
        clone.setOrder(original.getOrder());

        context.cacheClone(original, clone);

        return clone;
    }
}
