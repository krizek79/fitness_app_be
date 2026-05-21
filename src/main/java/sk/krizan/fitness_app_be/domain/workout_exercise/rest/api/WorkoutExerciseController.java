package sk.krizan.fitness_app_be.domain.workout_exercise.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.domain.workout_exercise.rest.dto.response.WorkoutExerciseResponse;

@Tag(
        name = "WorkoutExercise",
        description = "Manage workout exercises"
)
@RequestMapping("workout-exercises")
public interface WorkoutExerciseController {

    @Operation(
            summary = "Get a workout exercise by ID",
            description = "Returns the workout exercise with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout exercise returned successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout exercise not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    WorkoutExerciseResponse getWorkoutExerciseById(@PathVariable Long id);

}
