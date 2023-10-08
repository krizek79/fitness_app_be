package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
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
}
