package sk.krizan.fitness_app_be.domain.workout_exercise.mapper;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.mapper.WorkoutExerciseSetMapper;

import java.util.Comparator;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseMapper {

    public static WorkoutExerciseResponse entityToResponse(WorkoutExercise workoutExercise) {
        return WorkoutExerciseResponse.builder()
                .id(workoutExercise.getId())
                .workoutId(workoutExercise.getWorkout().getId())
                .order(workoutExercise.getOrder())
                .exerciseName(workoutExercise.getExercise().getName())
                .workoutExerciseType(ReferenceDataMapper.enumToResponse(workoutExercise.getWorkoutExerciseType()))
                .note(workoutExercise.getNote())
                .workoutExerciseSets(
                        workoutExercise.getWorkoutExerciseSets().stream()
                                .sorted(Comparator.comparingInt(WorkoutExerciseSet::getOrder))
                                .map(WorkoutExerciseSetMapper::entityToResponse)
                                .toList())
                .build();
    }

    public static WorkoutExercise inputRequestToEntity(
            @Nullable WorkoutExercise workoutExercise,
            WorkoutExerciseInputRequest request,
            Workout workout,
            Exercise exercise
    ) {
        if (workoutExercise == null) {
            workoutExercise = new WorkoutExercise();
            workout.addToWorkoutExercises(workoutExercise);
        }

        workoutExercise.setExercise(exercise);
        workoutExercise.setOrder(request.order());
        workoutExercise.setWorkoutExerciseType(request.workoutExerciseType());
        workoutExercise.setNote(request.note());

        return workoutExercise;
    }

}
