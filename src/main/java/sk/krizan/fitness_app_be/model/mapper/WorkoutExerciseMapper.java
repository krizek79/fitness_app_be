package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseMapper {

    public static WorkoutExercise createRequestToEntity(
        WorkoutExerciseCreateRequest request,
        Workout workout,
        Exercise exercise
    ) {
        return WorkoutExercise.builder()
            .workout(workout)
            .exercise(exercise)
            .sets(request.sets())
            .repetitions(request.repetitions())
            .build();
    }

    public static WorkoutExerciseResponse entityToResponse(WorkoutExercise workoutExercise) {
        return WorkoutExerciseResponse.builder()
            .id(workoutExercise.getId())
            .workoutId(workoutExercise.getWorkout().getId())
            .exerciseName(workoutExercise.getExercise().getName())
            .sets(workoutExercise.getSets())
            .repetitions(workoutExercise.getRepetitions())
            .build();
    }

    public static WorkoutExercise updateRequestToEntity(
        WorkoutExercise workoutExercise,
        WorkoutExerciseUpdateRequest request
    ) {
        workoutExercise.setSets(request.sets());
        workoutExercise.setRepetitions(request.repetitions());

        return workoutExercise;
    }
}
