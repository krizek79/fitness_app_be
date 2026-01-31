package sk.krizan.fitness_app_be.domain.workout.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.tag.entity.Tag;
import sk.krizan.fitness_app_be.domain.tag.mapper.TagMapper;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

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
        workout.setTitle(request.title());
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
                .name(workout.getTitle())
                .authorId(workout.getAuthor().getId())
                .authorName(workout.getAuthor().getName())
                .traineeId(workout.getTrainee() != null ? workout.getTrainee().getId() : null)
                .traineeName(workout.getTrainee() != null ? workout.getTrainee().getName() : null)
                .tagResponseList(
                        workout.getTagSet().stream()
                                .map(TagMapper::entityToResponse).toList())
                .description(workout.getDescription())
                .isTemplate(workout.getIsTemplate())
                .weightUnitResponse(ReferenceDataMapper.enumToResponse(workout.getWeightUnit()))
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
        workout.setTitle(request.title());
        workout.setDescription(request.description());
        workout.setWeightUnit(request.weightUnit());
        workout.getTagSet().clear();
        workout.addToTagSet(tagSet);
        workout.setNote(request.note());

        return workout;
    }
}
