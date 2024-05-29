package sk.krizan.fitness_app_be.controller.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WorkoutFilterRequest(
    @NotNull
    Integer page,
    @NotNull
    Integer size,
    @NotNull
    String sortBy,
    @NotNull
    String sortDirection,
    String name,
    String levelKey,
    List<String> tagNameList
) {
}
