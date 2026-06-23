package sk.krizan.fitness_app_be.domain.workout_exercise_set_result.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.entity.WorkoutExerciseSession;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.entity.WorkoutExerciseSet;
import sk.krizan.fitness_app_be.domain.workout_exercise_set.repository.WorkoutExerciseSetRepository;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.entity.WorkoutExerciseSetResult;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.mapper.WorkoutExerciseSetResultMapper;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request.WorkoutExerciseSetResultInputRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.service.api.WorkoutExerciseSetResultService;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseSetResultServiceImpl implements WorkoutExerciseSetResultService {

    private final WorkoutExerciseSetRepository workoutExerciseSetRepository;

    @Override
    public void createUpdateWorkoutExerciseSetResult(WorkoutExerciseSession workoutExerciseSession, WorkoutExerciseSetResultInputRequest request) {
        WorkoutExerciseSet workoutExerciseSet = resolveWorkoutExerciseSet(request);

        WorkoutExerciseSetResult result;
        if (request.id() != null) {
            result = workoutExerciseSession.getWorkoutExerciseSetResults().stream()
                    .filter(r -> r.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                            "WorkoutExerciseSetResult with id %d not found in WorkoutExerciseSession with id %d.".formatted(request.id(), workoutExerciseSession.getId())));
            WorkoutExerciseSetResultMapper.inputRequestToEntity(result, request, workoutExerciseSession, workoutExerciseSet);
        } else {
            WorkoutExerciseSetResultMapper.inputRequestToEntity(null, request, workoutExerciseSession, workoutExerciseSet);
        }
    }

    private WorkoutExerciseSet resolveWorkoutExerciseSet(WorkoutExerciseSetResultInputRequest request) {
        if (request.workoutExerciseSetId() == null) {
            return null;
        }
        return workoutExerciseSetRepository.getByIdOrThrow(request.workoutExerciseSetId());
    }

}
