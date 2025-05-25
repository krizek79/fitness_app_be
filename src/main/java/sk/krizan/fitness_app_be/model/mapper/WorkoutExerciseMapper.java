package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.model.entity.Exercise;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;

import java.time.Duration;
import java.util.List;

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
                .order(request.order())
                .sets(request.sets())
                .repetitions(request.repetitions())
                .restDuration(request.restDuration() != null ? Duration.parse(request.restDuration()) : null)
                .build();
    }

    public static WorkoutExerciseResponse entityToResponse(WorkoutExercise workoutExercise) {
        return WorkoutExerciseResponse.builder()
                .id(workoutExercise.getId())
                .workoutId(workoutExercise.getWorkout().getId())
                .order(workoutExercise.getOrder())
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
        workoutExercise.setOrder(request.order());
        workoutExercise.setRepetitions(request.repetitions());
        workoutExercise.setRestDuration(request.restDuration() != null ? Duration.parse(request.restDuration()) : null);

        return workoutExercise;
    }

    public static SimpleListResponse<WorkoutExerciseResponse> entityListToSimpleListResponse(List<WorkoutExercise> workoutExerciseList) {
        return SimpleListResponse.<WorkoutExerciseResponse>builder()
                .result(workoutExerciseList.stream().map(WorkoutExerciseMapper::entityToResponse).toList())
                .build();
    }
}
