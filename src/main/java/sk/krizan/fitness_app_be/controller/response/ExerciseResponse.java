package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ExerciseResponse(
        Long id,
        String name,
        List<EnumResponse> muscleGroupResponseList
) {
}
