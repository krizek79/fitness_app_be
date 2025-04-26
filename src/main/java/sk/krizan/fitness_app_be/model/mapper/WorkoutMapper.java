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

import java.util.Set;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutMapper {

    public static Workout createRequestToEntity(WorkoutCreateRequest request, Profile profile) {
        Workout workout = new Workout();
        workout.setAuthor(profile);
        workout.setName(request.name());
        return workout;
    }

    public static WorkoutResponse entityToResponse(Workout workout) {
        return WorkoutResponse.builder()
                .id(workout.getId())
                .name(workout.getName())
                .authorId(workout.getAuthor().getId())
                .authorName(workout.getAuthor().getName())
                .tagResponseList(
                        workout.getTagSet().stream()
                                .map(TagMapper::entityToResponse).toList())
                .description(workout.getDescription())
                .build();
    }

    public static Workout updateRequestToEntity(
            WorkoutUpdateRequest request,
            Workout workout,
            Set<Tag> tags
    ) {
        workout.setName(request.name());
        workout.setDescription(request.description());
        workout.getTagSet().clear();
        workout.getTagSet().addAll(tags);

        return workout;
    }
}
