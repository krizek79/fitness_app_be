package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseDetailResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSimpleResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.time.Duration;

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
                .restDuration(request.restDuration() != null ? Duration.parse(request.restDuration()) : null)
                .build();
    }

    public static WorkoutExerciseDetailResponse entityToDetailResponse(WorkoutExercise workoutExercise) {
        return WorkoutExerciseDetailResponse.builder()
                .id(workoutExercise.getId())
                .workoutId(workoutExercise.getWorkout().getId())
                .exerciseName(workoutExercise.getExercise().getName())
                .sets(workoutExercise.getSets())
                .repetitions(workoutExercise.getRepetitions())
                .restDuration(
                        workoutExercise.getRestDuration() != null
                                ? workoutExercise.getRestDuration().toString()
                                : null)
                .build();
    }

    public static WorkoutExerciseSimpleResponse entityToSimpleResponse(WorkoutExercise workoutExercise) {
        return WorkoutExerciseSimpleResponse.builder()
                .id(workoutExercise.getId())
                .workoutId(workoutExercise.getWorkout().getId())
                .exerciseName(workoutExercise.getExercise().getName())
                .sets(workoutExercise.getSets())
                .repetitions(workoutExercise.getRepetitions())
                .restDuration(
                        workoutExercise.getRestDuration() != null
                                ? workoutExercise.getRestDuration().toString()
                                : null)
                .build();
    }

    public static WorkoutExercise updateRequestToEntity(
            WorkoutExercise workoutExercise,
            WorkoutExerciseUpdateRequest request
    ) {
        workoutExercise.setSets(request.sets());
        workoutExercise.setRepetitions(request.repetitions());
        workoutExercise.setRestDuration(request.restDuration() != null ? Duration.parse(request.restDuration()) : null);

        return workoutExercise;
    }
}
