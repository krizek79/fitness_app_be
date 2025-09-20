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
    public Workout clone(Workout original) {
        Workout clone = new Workout();
        clone.setName(original.getName());
        clone.setDescription(original.getDescription());
        clone.addToTagSet(original.getTagSet());
        clone.setWeightUnit(original.getWeightUnit());
        List<WorkoutExercise> clonedWorkoutExercises = original.getWorkoutExerciseList().stream()
                .map(workoutExerciseCloner::clone)
                .toList();
        clone.addToWorkoutExerciseList(clonedWorkoutExercises);
        original.getAuthor().addToAuthoredWorkoutList(List.of(clone));

        return clone;
    }
}
