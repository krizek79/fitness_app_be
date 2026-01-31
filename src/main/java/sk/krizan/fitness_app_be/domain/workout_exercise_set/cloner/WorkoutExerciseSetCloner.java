package sk.krizan.fitness_app_be.domain.workout_exercise_set.cloner;

import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.cloning.AbstractCloner;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;

@Component
public class WorkoutExerciseSetCloner extends AbstractCloner<WorkoutExerciseSet> {

    @Override
    public Class<WorkoutExerciseSet> getHandledClass() {
        return WorkoutExerciseSet.class;
    }

    @Override
    public WorkoutExerciseSet clone(WorkoutExerciseSet original) {
        WorkoutExerciseSet clone = new WorkoutExerciseSet();
        clone.setOrder(original.getOrder());
        clone.setWorkoutExerciseSetType(original.getWorkoutExerciseSetType());
        clone.setRestDuration(original.getRestDuration());
        //  Set only goal attributes
        clone.setGoalRepetitions(original.getGoalRepetitions());
        clone.setGoalWeight(original.getGoalWeight());
        clone.setGoalTime(original.getGoalTime());

        return clone;
    }
}
