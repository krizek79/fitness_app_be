package sk.krizan.fitness_app_be.domain.workout_exercise.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.exercise.entity.Exercise;
import sk.krizan.fitness_app_be.domain.exercise.service.api.ExerciseService;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.mapper.WorkoutExerciseMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise.repository.WorkoutExerciseRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.service.api.WorkoutExerciseService;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.rest.dto.request.WorkoutExerciseSetInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.service.api.WorkoutExerciseSetService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final ExerciseService exerciseService;
    private final WorkoutExerciseSetService workoutExerciseSetService;

    private final WorkoutExerciseRepository workoutExerciseRepository;

    /**
     * Retrieves a workout exercise by its unique identifier.
     *
     * @param id the unique identifier of the workout exercise to be retrieved. This identifier is typically a numeric value that uniquely identifies a specific workout exercise in the system.
     * @return the workout exercise corresponding to the provided identifier. If no workout exercise is found with the given identifier, an appropriate exception may be thrown.
     * @throws ApplicationException if no workout exercise is found with the provided identifier. The exception may include details about the error, such as an error message and an HTTP status code indicating the nature of the error (e.g., 404 Not Found).
     */
    @Override
    public WorkoutExercise getWorkoutExerciseById(Long id) {
        return workoutExerciseRepository.getByIdOrThrow(id);
    }

    /**
     * Creates a new workout exercise or updates an existing one based on the provided input request. If the input request contains an identifier for an existing workout exercise, the method will update the corresponding workout exercise with the new data. If no identifier is provided, a new workout exercise will be created and associated with the specified workout.
     *
     * @param workout the workout to which the workout exercise will be associated. This parameter is typically an instance of the Workout entity that represents the workout to which the exercise belongs.
     * @param workoutExerciseInputRequest the input request containing the data for creating or updating the workout exercise. This request may include attributes such as exercise details, order, and other relevant information needed to create or update the workout exercise.
     */
    @Override
    public void createUpdateWorkoutExercise(Workout workout, WorkoutExerciseInputRequest workoutExerciseInputRequest) {
        Exercise exercise = exerciseService.getExerciseById(workoutExerciseInputRequest.exerciseId());

        WorkoutExercise workoutExercise;
        if (workoutExerciseInputRequest.id() != null) {
            //  Update existing workout exercise
            workoutExercise = workout.getWorkoutExercises().stream()
                    .filter(we -> we.getId().equals(workoutExerciseInputRequest.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Workout exercise with id %d not found in workout with id %d.".formatted(workoutExerciseInputRequest.id(), workout.getId())));
            WorkoutExerciseMapper.inputRequestToEntity(workoutExercise, workoutExerciseInputRequest, workout, exercise);
        } else {
            //  Create new workout exercise
            workoutExercise = WorkoutExerciseMapper.inputRequestToEntity(null, workoutExerciseInputRequest, workout, exercise);
        }

        //  Synchronize workout exercise sets
        resolveWorkoutExerciseSets(workoutExercise, workoutExerciseInputRequest.workoutExerciseSets());
    }

    /**
     * Resolves the workout exercise sets for a given workout exercise based on the incoming list of workout exercise set input requests. This method ensures that the workout exercise sets associated with the workout exercise are updated to reflect the changes specified in the input requests. It handles the creation of new workout exercise sets, updates to existing sets, and removal of sets that are no longer present in the incoming request.
     *
     * @param workoutExercise the workout exercise for which the workout exercise sets are being resolved. This parameter is typically an instance of the WorkoutExercise entity that represents the workout exercise to which the sets belong.
     * @param workoutExerciseSets the list of workout exercise set input requests that contain the data for creating, updating, or removing workout exercise sets. Each input request may include attributes such as set details, order, and other relevant information needed to manage the workout exercise sets.
     */
    private void resolveWorkoutExerciseSets(WorkoutExercise workoutExercise, List<WorkoutExerciseSetInputRequest> workoutExerciseSets) {
        Set<Long> incomingIds = workoutExerciseSets.stream().map(WorkoutExerciseSetInputRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        // Remove sets that are not in the incoming request
        workoutExercise.getWorkoutExerciseSets().removeIf(workoutExerciseSet -> workoutExerciseSet.getId() != null && !incomingIds.contains(workoutExerciseSet.getId()));

        // Add or update sets from the incoming request
        for (WorkoutExerciseSetInputRequest workoutExerciseSetInputRequest : workoutExerciseSets) {
            workoutExerciseSetService.createOrUpdateWorkoutExerciseSet(workoutExercise, workoutExerciseSetInputRequest);
        }

        //  Ensure the order of workout exercise sets is consistent with the input request
        workoutExercise.getWorkoutExerciseSets().sort(Comparator.comparing(
                WorkoutExerciseSet::getOrder,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));

        int currentOrder = 1;
        for (WorkoutExerciseSet workoutExerciseSet : workoutExercise.getWorkoutExerciseSets()) {
            workoutExerciseSet.setOrder(currentOrder++);
        }
    }

}
