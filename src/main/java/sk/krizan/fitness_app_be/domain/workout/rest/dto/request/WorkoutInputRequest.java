package sk.krizan.fitness_app_be.domain.workout.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.tag.rest.dto.request.TagCreateRequest;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.request.WorkoutExerciseInputRequest;

import java.util.List;
import java.util.Set;

@Builder
@FieldNameConstants
public record WorkoutInputRequest(

        Long traineeId,

        @NotEmpty
        @Size(min = 1, max = 64)
        String title,

        @Size(max = 1000)
        String description,

        @NotNull
        WeightUnit weightUnit,

        @Length(max = 1024)
        String note,

        @Schema(description = "Should be set only during creation and cannot be updated later.")
        @NotNull(groups = CreateGroup.class)
        Boolean isTemplate,

        @Valid
        @NotNull
        Set<TagCreateRequest> tags,

        @Valid
        @NotNull
        List<WorkoutExerciseInputRequest> workoutExercises
) {}
