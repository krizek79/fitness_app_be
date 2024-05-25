package sk.krizan.fitness_app_be.controller.request;

import java.util.List;
import lombok.Builder;

@Builder
public record WorkoutFilterRequest(
    Integer page,
    Integer size,
    String sortBy,
    String sortDirection,
    String name,
    String levelKey,
    List<String> tagNameList
) {
}