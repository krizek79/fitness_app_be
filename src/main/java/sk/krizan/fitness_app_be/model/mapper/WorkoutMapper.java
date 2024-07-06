package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Tag;
import sk.krizan.fitness_app_be.model.entity.Workout;
import sk.krizan.fitness_app_be.model.enums.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutMapper {

    public static Workout createRequestToEntity(WorkoutCreateRequest request, Profile profile) {
        return Workout.builder()
            .name(request.name())
            .author(profile)
            .tags(new HashSet<>())
            .workoutExerciseList(new ArrayList<>())
            .build();
    }

    public static WorkoutSimpleResponse entityToSimpleResponse(Workout workout) {
        return WorkoutSimpleResponse.builder()
            .id(workout.getId())
            .name(workout.getName())
            .authorName(workout.getAuthor().getName())
            .tagResponseList(
                workout.getTags().stream()
                    .map(TagMapper::entityToResponse).toList())
            .levelValue(workout.getLevel() == null ? null : workout.getLevel().getValue())
            .build();
    }

    public static WorkoutDetailResponse entityToDetailResponse(Workout workout) {
        return WorkoutDetailResponse.builder()
                .id(workout.getId())
                .name(workout.getName())
                .authorName(workout.getAuthor().getName())
                .tagResponseList(
                        workout.getTags().stream()
                                .map(TagMapper::entityToResponse).toList())
                .levelValue(workout.getLevel() == null ? null : workout.getLevel().getValue())
                .description(workout.getDescription())
                .workoutExerciseSimpleResponseList(
                        workout.getWorkoutExerciseList().stream()
                                .map(WorkoutExerciseMapper::entityToSimpleResponse).toList())
                .build();
    }

    public static Workout updateRequestToEntity(
            WorkoutUpdateRequest request,
            Workout workout,
            Level level,
            Set<Tag> tags
    ) {
        workout.setName(request.name());
        workout.setDescription(request.description());
        workout.setLevel(level);
        workout.setTags(tags);

        return workout;
    }
}
