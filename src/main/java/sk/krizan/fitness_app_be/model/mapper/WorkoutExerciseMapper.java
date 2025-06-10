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
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseType;

import java.util.Comparator;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseMapper {

    public static WorkoutExercise createRequestToEntity(
            WorkoutExerciseCreateRequest request,
            Workout workout,
            Exercise exercise,
            WorkoutExerciseType workoutExerciseType
    ) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
        workoutExercise.setOrder(request.order());
        workoutExercise.setWorkoutExerciseType(workoutExerciseType);
        workoutExercise.setNote(request.note());
        return workoutExercise;
    }

    public static WorkoutExerciseResponse entityToResponse(WorkoutExercise workoutExercise) {
        return WorkoutExerciseResponse.builder()
                .id(workoutExercise.getId())
                .workoutId(workoutExercise.getWorkout().getId())
                .order(workoutExercise.getOrder())
                .exerciseName(workoutExercise.getExercise().getName())
                .workoutExerciseTypeResponse(EnumMapper.enumToResponse(workoutExercise.getWorkoutExerciseType()))
                .note(workoutExercise.getNote())
                .workoutExerciseSetResponseList(
                        workoutExercise.getWorkoutExerciseSetList().stream()
                                .sorted(Comparator.comparingInt(WorkoutExerciseSet::getOrder))
                                .map(WorkoutExerciseSetMapper::entityToResponse)
                                .toList())
                .build();
    }

    public static WorkoutExercise updateRequestToEntity(
            WorkoutExercise workoutExercise,
            WorkoutExerciseType workoutExerciseType,
            WorkoutExerciseUpdateRequest request
    ) {
        workoutExercise.setOrder(request.order());
        workoutExercise.setWorkoutExerciseType(workoutExerciseType);
        workoutExercise.setNote(request.note());

        return workoutExercise;
    }

    public static SimpleListResponse<WorkoutExerciseResponse> entityListToSimpleListResponse(List<WorkoutExercise> workoutExerciseList) {
        return SimpleListResponse.<WorkoutExerciseResponse>builder()
                .result(workoutExerciseList.stream().map(WorkoutExerciseMapper::entityToResponse).toList())
                .build();
    }
}
