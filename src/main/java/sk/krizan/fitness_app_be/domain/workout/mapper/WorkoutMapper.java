package sk.krizan.fitness_app_be.domain.workout.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.mapper.ProfileMapper;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;
import sk.krizan.fitness_app_be.domain.tag.mapper.TagMapper;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout_exercise.entity.WorkoutExercise;
import sk.krizan.fitness_app_be.domain.workout_exercise.mapper.WorkoutExerciseMapper;

import java.util.Comparator;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutMapper {

    public static WorkoutSimpleResponse entityToSimpleResponse(Workout workout) {
        return WorkoutSimpleResponse.builder()
                .id(workout.getId())
                .title(workout.getTitle())
                .author(ProfileMapper.entityToSimpleResponse(workout.getAuthor()))
                .trainee(ProfileMapper.entityToSimpleResponse(workout.getTrainee()))
                .tags(workout.getTags().stream().map(TagMapper::entityToResponse).toList())
                .isTemplate(workout.getIsTemplate())
                .build();
    }

    public static WorkoutDetailResponse entityToDetailResponse(Workout workout) {
        return WorkoutDetailResponse.builder()
                .id(workout.getId())
                .title(workout.getTitle())
                .author(ProfileMapper.entityToSimpleResponse(workout.getAuthor()))
                .trainee(ProfileMapper.entityToSimpleResponse(workout.getTrainee()))
                .tags(workout.getTags().stream().map(TagMapper::entityToResponse).toList())
                .description(workout.getDescription())
                .isTemplate(workout.getIsTemplate())
                .weightUnit(ReferenceDataMapper.enumToResponse(workout.getWeightUnit()))
                .distanceUnit(ReferenceDataMapper.enumToResponse(workout.getDistanceUnit()))
                .note(workout.getNote())
                .workoutExercises(workout.getWorkoutExercises().stream()
                        .sorted(Comparator.comparing(WorkoutExercise::getOrder))
                        .map(WorkoutExerciseMapper::entityToResponse)
                        .toList())
                .build();
    }

    public static void inputRequestToEntity(
            boolean isNew,
            WorkoutInputRequest request,
            Workout workout,
            Profile author,
            Profile trainee
    ) {
        if (isNew) {
            workout.setIsTemplate(request.isTemplate());
            author.addToAuthoredWorkouts(workout);

            if (!request.isTemplate()) {
                trainee.addToAssignedWorkouts(workout);
            }
        }

        //  If the workout already has a trainee, and it's different from the new trainee, we need to update the assigned workouts for both trainees
        Profile originalTrainee = workout.getTrainee();
        if (originalTrainee != null && !originalTrainee.equals(trainee)) {
            originalTrainee.removeFromAssignedWorkouts(workout);
            trainee.addToAssignedWorkouts(workout);
        }

        workout.setTitle(request.title());
        workout.setDescription(request.description());

        workout.setWeightUnit(request.weightUnit());
        workout.setDistanceUnit(request.distanceUnit());
        workout.setNote(request.note());
    }
}
