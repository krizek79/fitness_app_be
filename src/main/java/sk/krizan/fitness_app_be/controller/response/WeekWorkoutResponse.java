package sk.krizan.fitness_app_be.controller.response;

import lombok.Builder;

import java.util.List;

@Builder
public record WeekWorkoutResponse(
        Long id,
        Long weekId,
        Long workoutId,
        String workoutName,
        List<TagResponse> workoutTagResponseList,
        Integer dayOfTheWeek,
        Boolean completed
) {
}
