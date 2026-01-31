package sk.krizan.fitness_app_be.domain.workout_exercise.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.SimpleListResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.mapper.WorkoutExerciseSetMapper;

import java.util.Comparator;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseMapper {

    public static WorkoutExercise createRequestToEntity(
            WorkoutExerciseCreateRequest request,
            Workout workout,
            Exercise exercise
    ) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
        workoutExercise.setOrder(request.order());
        workoutExercise.setWorkoutExerciseType(request.workoutExerciseType());
        workoutExercise.setNote(request.note());
        return workoutExercise;
    }

    public static WorkoutExerciseResponse entityToResponse(WorkoutExercise workoutExercise) {
        return WorkoutExerciseResponse.builder()
                .id(workoutExercise.getId())
                .workoutId(workoutExercise.getWorkout().getId())
                .order(workoutExercise.getOrder())
                .exerciseName(workoutExercise.getExercise().getName())
                .workoutExerciseTypeResponse(ReferenceDataMapper.enumToResponse(workoutExercise.getWorkoutExerciseType()))
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
            WorkoutExerciseUpdateRequest request
    ) {
        workoutExercise.setOrder(request.order());
        workoutExercise.setWorkoutExerciseType(request.workoutExerciseType());
        workoutExercise.setNote(request.note());

        return workoutExercise;
    }

    public static SimpleListResponse<WorkoutExerciseResponse> entityListToSimpleListResponse(List<WorkoutExercise> workoutExerciseList) {
        return SimpleListResponse.<WorkoutExerciseResponse>builder()
                .result(workoutExerciseList.stream().map(WorkoutExerciseMapper::entityToResponse).toList())
                .build();
    }
}
