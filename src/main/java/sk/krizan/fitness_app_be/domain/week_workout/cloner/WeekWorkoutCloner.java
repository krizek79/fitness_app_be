package sk.krizan.fitness_app_be.domain.week_workout.cloner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.common.cloning.AbstractCloner;
import sk.krizan.fitness_app_be.domain.workout.cloner.WorkoutCloner;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WeekWorkout;

@Component
@RequiredArgsConstructor
public class WeekWorkoutCloner extends AbstractCloner<WeekWorkout> {

    private final WorkoutCloner workoutCloner;

    @Override
    public Class<WeekWorkout> getHandledClass() {
        return WeekWorkout.class;
    }

    @Override
    public WeekWorkout clone(WeekWorkout original) {
        WeekWorkout clone = new WeekWorkout();
        clone.setCompleted(false);
        clone.setDayOfWeek(original.getDayOfWeek());
        clone.setWorkout(workoutCloner.clone(original.getWorkout()));

        return clone;
    }
}
