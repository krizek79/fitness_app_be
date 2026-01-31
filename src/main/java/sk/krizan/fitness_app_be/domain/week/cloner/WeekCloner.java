package sk.krizan.fitness_app_be.domain.week.cloner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.cloning.AbstractCloner;
import sk.krizan.fitness_app_be.domain.week_workout.cloner.WeekWorkoutCloner;
import sk.krizan.fitness_app_be.domain.week.entity.Week;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;

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
    public Week clone(Week original) {
        Week clone = new Week();
        clone.setOrder(original.getOrder());
        clone.setCompleted(false);
        List<WeekWorkout> clonedWeekWorkouts = original.getWeekWorkoutList().stream()
                .map(weekWorkoutCloner::clone)
                .toList();
        clone.addToWeekWorkoutList(clonedWeekWorkouts);

        return clone;
    }
}
