package sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.response.TagResponse;

import java.util.List;

@Builder
@FieldNameConstants
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
