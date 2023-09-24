package sk.krizan.fitness_app_be.controller.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ExerciseResponse(
    Long id,
    String name,
    List<String> muscleGroupValues
) {
}
