package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

import java.util.List;

@Builder
public record WorkoutResponse(
        Long id,
        String name,
        String authorName,
        List<TagResponse> tagResponseList,
        String levelValue,
        String description
) {
}
