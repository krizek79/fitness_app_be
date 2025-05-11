package sk.krizan.fitness_app_be.cloning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;

@Component
@RequiredArgsConstructor
public class WeekWorkoutCloner extends AbstractCloner<WeekWorkout> {

    private final WorkoutCloner workoutCloner;

    @Override
    public Class<WeekWorkout> getHandledClass() {
        return WeekWorkout.class;
    }

    @Override
    public WeekWorkout clone(WeekWorkout original, CloneContext context) {
        if (context.isAlreadyCloned(original)) {
            return context.getCachedClone(original);
        }

        WeekWorkout clone = new WeekWorkout();
        clone.setCompleted(false);
        clone.setDayOfTheWeek(original.getDayOfTheWeek());
        clone.setWorkout(workoutCloner.clone(original.getWorkout(), context));

        context.cacheClone(original, clone);

        return clone;
    }
}
