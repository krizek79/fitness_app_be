package sk.krizan.fitness_app_be.controller.response;

import java.util.List;
import lombok.Builder;

@Builder
public record WorkoutResponse(
    Long id,
    String name,
    String authorName,
    List<TagResponse> tagResponseList,
    String levelValue
) {
}
