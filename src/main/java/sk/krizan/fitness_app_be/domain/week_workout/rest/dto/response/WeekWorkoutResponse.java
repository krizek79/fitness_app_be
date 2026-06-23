package sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;

import java.time.DayOfWeek;

@Builder
@FieldNameConstants
public record WeekWorkoutResponse(
        Long id,
        Long weekId,
        WorkoutSimpleResponse workout,
        DayOfWeek dayOfWeek,
        Integer orderInTheDay,
        WorkoutStatus status
) {
}
