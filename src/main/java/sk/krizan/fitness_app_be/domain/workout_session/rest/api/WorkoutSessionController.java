package sk.krizan.fitness_app_be.domain.workout_session.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.common.validation.group.UpdateGroup;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionFilterRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.request.WorkoutSessionInputRequest;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionDetailResponse;
import sk.krizan.fitness_app_be.domain.workout_session.rest.dto.response.WorkoutSessionSimpleResponse;

@Tag(
        name = "WorkoutSession",
        description = "Manage workout sessions"
)
@RequestMapping("workout-sessions")
public interface WorkoutSessionController {

    @Operation(
            summary = "Filter workout sessions",
            description = "Filters workout sessions based on the provided criteria.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workout sessions retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid filter parameters",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PostMapping("filter")
    PageResponse<WorkoutSessionSimpleResponse> filterWorkoutSessions(@Valid @RequestBody WorkoutSessionFilterRequest request);

    @Operation(
            summary = "Get workout session by ID",
            description = "Returns the workout session with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workout session returned successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutSessionDetailResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "404", description = "Workout session not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    WorkoutSessionDetailResponse getWorkoutSessionById(@PathVariable Long id);

    @Operation(
            summary = "Create workout session",
            description = "Creates a new workout session for the specified workout.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Workout session created successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutSessionDetailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    WorkoutSessionDetailResponse createWorkoutSession(@Valid @Validated(CreateGroup.class) @RequestBody WorkoutSessionInputRequest request);

    @Operation(
            summary = "Update workout session",
            description = "Updates the workout session with the specified ID, including its exercise sessions and set results.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workout session updated successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutSessionDetailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "404", description = "Workout session not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping("{id}")
    WorkoutSessionDetailResponse updateWorkoutSession(@PathVariable Long id, @Valid @Validated(UpdateGroup.class) @RequestBody WorkoutSessionInputRequest request);

    @Operation(
            summary = "Delete workout session",
            description = "Deletes the workout session with the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Workout session deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "404", description = "Workout session not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteWorkoutSession(@PathVariable Long id);

}
