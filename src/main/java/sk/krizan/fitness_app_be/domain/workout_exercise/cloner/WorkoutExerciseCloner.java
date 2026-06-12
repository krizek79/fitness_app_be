package sk.krizan.fitness_app_be.domain.workout_exercise.cloner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.common.cloning.AbstractCloner;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.cloner.WorkoutExerciseSetCloner;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;

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
    public WorkoutExercise clone(WorkoutExercise original) {
        WorkoutExercise clone = new WorkoutExercise();
        clone.setExercise(original.getExercise());
        clone.setOrder(original.getOrder());
        clone.setWorkoutExerciseMetric(original.getWorkoutExerciseMetric());

        List<WorkoutExerciseSet> clonedWorkoutExercises = original.getWorkoutExerciseSets().stream()
                .map(workoutExerciseSetCloner::clone)
                .toList();
        clonedWorkoutExercises.forEach(clone::addToWorkoutExerciseSets);

        return clone;
    }
}
