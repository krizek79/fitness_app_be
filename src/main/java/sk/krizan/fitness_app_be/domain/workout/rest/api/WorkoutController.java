package sk.krizan.fitness_app_be.domain.workout.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.common.validation.group.UpdateGroup;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutDetailResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.response.WorkoutSimpleResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.wrapper.WorkoutPageResponse;
import sk.krizan.fitness_app_be.domain.workout.rest.dto.request.WorkoutFilterRequest;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;

@Tag(
        name = "Workout",
        description = "Manage workouts"
)
@RequestMapping("workouts")
public interface WorkoutController {

    @Operation(
            summary = "Filter workouts",
            description = "Filters workouts based on the provided criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workouts retrieved successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutPageResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid filter parameters",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PostMapping("filter")
    PageResponse<WorkoutSimpleResponse> filterWorkouts(@Valid @RequestBody WorkoutFilterRequest request);


    @Operation(
            summary = "Get workout by ID",
            description = "Returns workout with given ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout found",
                            content = @Content(schema = @Schema(implementation = WorkoutDetailResponse.class))),
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
                            description = "Workout not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    WorkoutDetailResponse getWorkoutById(@PathVariable Long id);

    @Operation(
            summary = "Create workout",
            description = "Creates a new workout with tags, workout exercises and sets.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Workout created successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutDetailResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    WorkoutDetailResponse createWorkout(@Valid @Validated(CreateGroup.class) @RequestBody WorkoutInputRequest request);

    @Operation(
            summary = "Update workout",
            description = "Updates the workout with the specified ID, including its tags, workout exercises and sets.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout updated successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutDetailResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
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
                            description = "Workout not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping("{id}")
    WorkoutDetailResponse updateWorkout(@PathVariable Long id, @Valid @Validated(UpdateGroup.class) @RequestBody WorkoutInputRequest request);

    @Operation(
            summary = "Delete workout",
            description = "Deletes the workout with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout deleted successfully",
                            content = @Content(schema = @Schema())),
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
                            description = "Workout not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteWorkout(@PathVariable Long id);

}
