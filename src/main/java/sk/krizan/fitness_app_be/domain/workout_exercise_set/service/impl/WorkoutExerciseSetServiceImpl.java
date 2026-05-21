package sk.krizan.fitness_app_be.domain.workout_exercise_set.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.mapper.WorkoutExerciseSetMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.service.api.WorkoutExerciseSetService;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseSetServiceImpl implements WorkoutExerciseSetService {

    /**
     * Creates a new workout exercise set or updates an existing one based on the provided input request. If the input request contains an identifier for an existing workout exercise set, the method will update that set with the new information. If no identifier is provided, a new workout exercise set will be created and associated with the specified workout exercise.
     *
     * @param workoutExercise the workout exercise to which the workout exercise set will be associated. This parameter is required for both creating a new workout exercise set and updating an existing one, as it establishes the relationship between the workout exercise set and its parent workout exercise.
     * @param workoutExerciseSetInputRequest the input request containing the data for creating or updating the workout exercise set. This request may include attributes such as the order of the set, the number of repetitions, the weight used, and other relevant information needed to create or update the workout exercise set.
     */
    @Override
    public void createOrUpdateWorkoutExerciseSet(WorkoutExercise workoutExercise, WorkoutExerciseSetInputRequest workoutExerciseSetInputRequest) {
        WorkoutExerciseSet workoutExerciseSet;
        if (workoutExerciseSetInputRequest.id() != null) {
            //  Update existing workout exercise set
            workoutExerciseSet = workoutExercise.getWorkoutExerciseSets().stream()
                    .filter(we -> we.getId().equals(workoutExerciseSetInputRequest.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Workout exercise set with id %d not found in workout exercise with id %d.".formatted(workoutExerciseSetInputRequest.id(), workoutExercise.getId())));
            WorkoutExerciseSetMapper.inputRequestToEntity(workoutExerciseSet, workoutExerciseSetInputRequest, workoutExercise);
        } else {
            //  Create new workout exercise set
            WorkoutExerciseSetMapper.inputRequestToEntity(null, workoutExerciseSetInputRequest, workoutExercise);
        }
    }
}
