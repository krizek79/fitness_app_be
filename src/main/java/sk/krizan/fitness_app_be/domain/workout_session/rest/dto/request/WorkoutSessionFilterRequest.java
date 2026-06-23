package sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;

@Builder
@FieldNameConstants
public record WorkoutSessionFilterRequest(

        @NotNull
        @Min(0)
        Integer page,

        @NotNull
        @Min(1)
        Integer size,

        @NotNull
        String sortBy,

        @NotNull
        @Pattern(regexp = "ASC|DESC|asc|desc")
        String sortDirection,

        Long workoutId,

        Long weekWorkoutId,

        Long profileId,

        WorkoutStatus status

) {}
