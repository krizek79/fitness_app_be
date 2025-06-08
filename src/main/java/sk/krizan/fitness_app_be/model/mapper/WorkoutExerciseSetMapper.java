package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.model.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.model.enums.WorkoutExerciseSetType;

import java.time.Duration;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutExerciseSetMapper {

    public static WorkoutExerciseSetResponse entityToResponse(WorkoutExerciseSet workoutExerciseSet) {
        return WorkoutExerciseSetResponse.builder()
                .id(workoutExerciseSet.getId())
                .workoutExerciseId(workoutExerciseSet.getWorkoutExercise().getId())
                .order(workoutExerciseSet.getOrder())
                .workoutExerciseSetTypeResponse(EnumMapper.enumToResponse(workoutExerciseSet.getWorkoutExerciseSetType()))
                .goalRepetitions(workoutExerciseSet.getGoalRepetitions())
                .actualRepetitions(workoutExerciseSet.getActualRepetitions())
                .goalWeight(workoutExerciseSet.getGoalWeight())
                .actualWeight(workoutExerciseSet.getActualWeight())
                .goalTime(workoutExerciseSet.getGoalTime() != null ? workoutExerciseSet.getGoalTime().toString() : null)
                .actualTime(workoutExerciseSet.getActualTime() != null ? workoutExerciseSet.getActualTime().toString() : null)
                .restDuration(workoutExerciseSet.getRestDuration() != null ? workoutExerciseSet.getRestDuration().toString() : null)
                .completed(workoutExerciseSet.getCompleted())
                .note(workoutExerciseSet.getNote())
                .build();
    }

    public static WorkoutExerciseSet createRequestToEntity(WorkoutExerciseSetCreateRequest request, WorkoutExercise workoutExercise, WorkoutExerciseSetType workoutExerciseSetType) {
        WorkoutExerciseSet workoutExerciseSet = new WorkoutExerciseSet();
        workoutExerciseSet.setWorkoutExercise(workoutExercise);
        workoutExerciseSet.setOrder(request.order());
        workoutExerciseSet.setWorkoutExerciseSetType(workoutExerciseSetType);
        workoutExerciseSet.setGoalRepetitions(request.goalRepetitions());
        workoutExerciseSet.setGoalWeight(request.goalWeight());
        workoutExerciseSet.setGoalTime(request.goalTime() != null ? Duration.parse(request.goalTime()) : null);
        workoutExerciseSet.setRestDuration(request.restDuration() != null ? Duration.parse(request.restDuration()) : null);
        workoutExerciseSet.setNote(request.note());
        workoutExercise.addToWorkoutExerciseSetList(List.of(workoutExerciseSet));
        return workoutExerciseSet;
    }

    public static WorkoutExerciseSet updateRequestToEntity(WorkoutExerciseSetUpdateRequest request, WorkoutExerciseSet workoutExerciseSet, WorkoutExerciseSetType workoutExerciseSetType) {
        workoutExerciseSet.setOrder(request.order());
        workoutExerciseSet.setWorkoutExerciseSetType(workoutExerciseSetType);
        workoutExerciseSet.setGoalRepetitions(request.goalRepetitions());
        workoutExerciseSet.setActualRepetitions(request.actualRepetitions());
        workoutExerciseSet.setGoalWeight(request.goalWeight());
        workoutExerciseSet.setActualWeight(request.actualWeight());
        workoutExerciseSet.setGoalTime(request.goalTime() != null ? Duration.parse(request.goalTime()) : null);
        workoutExerciseSet.setActualTime(request.actualTime() != null ? Duration.parse(request.actualTime()) : null);
        workoutExerciseSet.setRestDuration(request.restDuration() != null ? Duration.parse(request.restDuration()) : null);
        workoutExerciseSet.setNote(request.note());
        return workoutExerciseSet;
    }

    public static SimpleListResponse<WorkoutExerciseSetResponse> entityListToSimpleListResponse(List<WorkoutExerciseSet> workoutExerciseSetList) {
        return SimpleListResponse.<WorkoutExerciseSetResponse>builder()
                .result(workoutExerciseSetList.stream().map(WorkoutExerciseSetMapper::entityToResponse).toList())
                .build();
    }
}
