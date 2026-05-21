package sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;

import java.time.DayOfWeek;

@Builder
@FieldNameConstants
public record WeekWorkoutInputRequest(

        @NotNull
        Long weekId,

        @Schema(description = "Should be set only if we want to create a clone from an existing workout template. If workoutToUpdateId is provided, this field is ignored.")
        Long workoutToCloneId,

        @Null(groups = CreateGroup.class)
        @Schema(description = "Should be set only if we want to update an already existing workout. (pair with " + WeekWorkoutInputRequest.Fields.workout + ")")
        Long workoutToUpdateId,

        @Valid
        @Schema(description = "Should be set only if we want to create a new workout or update the existing one. (pair with " + WeekWorkoutInputRequest.Fields.workoutToUpdateId + ")")
        WorkoutInputRequest workout,

        @NotNull
        DayOfWeek dayOfWeek,

        @NotNull
        @Min(1)
        @Schema(description = "Order of the workout within the same day. Defaults to 1.")
        Integer orderInTheDay,

        @NotNull
        @Null(groups = CreateGroup.class)
        Boolean completed
) {
}
