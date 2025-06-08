package sk.krizan.fitness_app_be.cloning;

import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;

@Component
public class WorkoutExerciseSetCloner extends AbstractCloner<WorkoutExerciseSet> {

    @Override
    public Class<WorkoutExerciseSet> getHandledClass() {
        return WorkoutExerciseSet.class;
    }

    @Override
    public WorkoutExerciseSet clone(WorkoutExerciseSet original, CloneContext context) {
        if (context.isAlreadyCloned(original)) {
            return context.getCachedClone(original);
        }

        WorkoutExerciseSet clone = new WorkoutExerciseSet();
        clone.setOrder(original.getOrder());
        clone.setWorkoutExerciseSetType(original.getWorkoutExerciseSetType());
        clone.setRestDuration(original.getRestDuration());
        //  Set only goal attributes
        clone.setGoalRepetitions(original.getGoalRepetitions());
        clone.setGoalWeight(original.getGoalWeight());
        clone.setGoalTime(original.getGoalTime());

        context.cacheClone(original, clone);

        return clone;
    }
}
