package sk.krizan.fitness_app_be.cloning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.Week;
import sk.krizan.fitness_app_be.model.entity.WeekWorkout;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WeekCloner extends AbstractCloner<Week> {

    private final WeekWorkoutCloner weekWorkoutCloner;

    @Override
    public Class<Week> getHandledClass() {
        return Week.class;
    }

    @Override
    public Week clone(Week original, CloneContext context) {
        if (context.isAlreadyCloned(original)) {
            return context.getCachedClone(original);
        }

        Week clone = new Week();
        clone.setOrder(original.getOrder());
        clone.setCompleted(false);
        List<WeekWorkout> clonedWeekWorkouts = original.getWeekWorkoutList().stream()
                .map(weekWorkout -> weekWorkoutCloner.clone(weekWorkout, context))
                .toList();
        clone.addToWeekWorkoutList(clonedWeekWorkouts);

        context.cacheClone(original, clone);

        return clone;
    }
}
