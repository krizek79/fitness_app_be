package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.Workout;

import java.util.List;
import java.util.Set;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutMapper {

    public static Workout createRequestToEntity(
            WorkoutCreateRequest request,
            Profile profile,
            Profile trainee,
            Set<Tag> tagSet
    ) {
        Workout workout = new Workout();
        workout.setAuthor(profile);
        workout.setTrainee(trainee);
        workout.setName(request.name());
        workout.setIsTemplate(request.isTemplate());
        workout.setWeightUnit(request.weightUnit());
        workout.setNote(request.note());
        workout.setDescription(request.description());
        workout.addToTagSet(tagSet);
        profile.addToAuthoredWorkoutList(List.of(workout));
        return workout;
    }

    public static WorkoutResponse entityToResponse(Workout workout) {
        return WorkoutResponse.builder()
                .id(workout.getId())
                .name(workout.getName())
                .authorId(workout.getAuthor().getId())
                .authorName(workout.getAuthor().getName())
                .traineeId(workout.getTrainee() != null ? workout.getTrainee().getId() : null)
                .traineeName(workout.getTrainee() != null ? workout.getTrainee().getName() : null)
                .tagResponseList(
                        workout.getTagSet().stream()
                                .map(TagMapper::entityToResponse).toList())
                .description(workout.getDescription())
                .isTemplate(workout.getIsTemplate())
                .weightUnitResponse(EnumMapper.enumToResponse(workout.getWeightUnit()))
                .note(workout.getNote())
                .build();
    }

    public static Workout updateRequestToEntity(
            WorkoutUpdateRequest request,
            Workout workout,
            Profile trainee,
            Set<Tag> tagSet
    ) {
        workout.setTrainee(trainee);
        workout.setName(request.name());
        workout.setDescription(request.description());
        workout.setWeightUnit(request.weightUnit());
        workout.getTagSet().clear();
        workout.addToTagSet(tagSet);
        workout.setNote(request.note());

        return workout;
    }
}
