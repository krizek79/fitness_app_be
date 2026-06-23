package sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.common.validation.group.UpdateGroup;
import sk.krizan.fitness_app_be.domain.week_workout.entity.WorkoutStatus;
import sk.krizan.fitness_app_be.domain.workout_exercise_session.rest.dto.request.WorkoutExerciseSessionInputRequest;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@FieldNameConstants
public record WorkoutSessionInputRequest(

        @NotNull(groups = CreateGroup.class)
        @Null(groups = UpdateGroup.class)
        @Schema(description = "ID of the workout to execute. Required on creation, ignored on update.")
        Long workoutId,

        @Null(groups = UpdateGroup.class)
        @Schema(description = "Optional ID of the WeekWorkout this session is associated with. Should be set only on creation.")
        Long weekWorkoutId,

        WorkoutStatus status,

        LocalDateTime startedAt,

        LocalDateTime finishedAt,

        @Valid
        @NotNull
        List<WorkoutExerciseSessionInputRequest> workoutExerciseSessions

) {}
