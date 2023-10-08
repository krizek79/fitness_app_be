package sk.krizan.fitness_app_be.model.mapper;

import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.WorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.response.WorkoutResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.Workout;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutMapper {

    public static Workout createRequestToEntity(WorkoutCreateRequest request, Profile profile) {
        return Workout.builder()
            .name(request.name())
            .author(profile)
            .tags(new ArrayList<>())
            .build();
    }

    public static WorkoutResponse entityToResponse(Workout workout) {
        return WorkoutResponse.builder()
            .id(workout.getId())
            .name(workout.getName())
            .authorName(workout.getAuthor().getName())
            .tagResponseList(
                workout.getTags().stream()
                    .map(TagMapper::entityToResponse).toList())
            .levelValue(workout.getLevel() == null ? null : workout.getLevel().getValue())
            .build();
    }
}
