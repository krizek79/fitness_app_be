package sk.krizan.fitness_app_be.domain.workout_exercise_set.service.api;

import sk.krizan.fitness_app_be.common.rest.dto.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.response.WorkoutExerciseSetResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;

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
