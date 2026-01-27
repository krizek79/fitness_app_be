package sk.krizan.fitness_app_be.service.api;

import sk.krizan.fitness_app_be.controller.request.common.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.workout_exercise_set.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.controller.request.workout_exercise_set.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.controller.request.workout_exercise_set.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.model.entity.WorkoutExerciseSet;

import java.util.List;

public interface WorkoutExerciseSetService {

    PageResponse<WorkoutExerciseSetResponse> filterWorkoutExerciseSets(WorkoutExerciseSetFilterRequest request);

    WorkoutExerciseSet getWorkoutExerciseSetById(Long id);

    WorkoutExerciseSet createWorkoutExerciseSet(WorkoutExerciseSetCreateRequest request);

    WorkoutExerciseSet updateWorkoutExerciseSet(WorkoutExerciseSetUpdateRequest request);

    List<WorkoutExerciseSet> batchUpdateWorkoutExerciseSets(BatchUpdateRequest<WorkoutExerciseSetUpdateRequest> request);

    Long deleteWorkoutExerciseSet(Long id);

    WorkoutExerciseSet triggerCompleted(Long id);
}
