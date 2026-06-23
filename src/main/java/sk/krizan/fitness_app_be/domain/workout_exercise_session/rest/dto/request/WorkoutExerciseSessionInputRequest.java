package sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.domain.workout_exercise_set_result.rest.dto.request.WorkoutExerciseSetResultInputRequest;

import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutExerciseSessionInputRequest(

        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Long id,

        @NotNull
        Long workoutExerciseId,

        @NotNull
        @Min(1)
        Integer order,

        @Length(max = 1024)
        String note,

        @Valid
        @NotNull
        List<WorkoutExerciseSetResultInputRequest> workoutExerciseSetResults

) {}
