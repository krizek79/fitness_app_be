package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.ExceptionResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseResponse;

@Tag(
        name = "WorkoutExercise",
        description = "Manage workout exercises"
)
@RequestMapping("workout-exercises")
public interface WorkoutExerciseController {

    @Operation(
            summary = "Filter workout exercises",
            description = "Returns a paginated list of workout exercises based on the filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filtered workout exercises returned successfully",
                            content = @Content(schema = @Schema(implementation = PageResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping("filter")
    PageResponse<WorkoutExerciseResponse> filterWorkoutExercises(@Valid @RequestBody WorkoutExerciseFilterRequest request);

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
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout exercise not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("{id}")
    WorkoutExerciseResponse getWorkoutExerciseById(@PathVariable Long id);

    @Operation(
            summary = "Create a new workout exercise",
            description = "Creates and returns a new workout exercise.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Workout exercise created successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    WorkoutExerciseResponse createWorkoutExercise(@Valid @RequestBody WorkoutExerciseCreateRequest request);

    @Operation(
            summary = "Update a workout exercise",
            description = """
                    Updates an existing workout exercise with the provided data.
                    
                    ⚠️ **Deprecated:** This endpoint should not be used for bulk updates.
                    For batch updates, please use the `batchUpdateWorkoutExercises` endpoint instead.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout exercise updated successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout exercise not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PatchMapping
    WorkoutExerciseResponse updateWorkoutExercise(@Valid @RequestBody WorkoutExerciseUpdateRequest request);

    @Operation(
            summary = "Batch update workout exercises",
            description = "Updates multiple workout exercises in one request and returns updated results.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout exercises updated successfully",
                            content = @Content(schema = @Schema(implementation = SimpleListResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping("batch-update")
    SimpleListResponse<WorkoutExerciseResponse> batchUpdateWorkoutExercises(@Valid @RequestBody BatchUpdateRequest<WorkoutExerciseUpdateRequest> request);

    @Operation(
            summary = "Delete a workout exercise",
            description = "Deletes a workout exercise by ID and returns the ID of the deleted entity.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout exercise deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout exercise not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteWorkoutExercise(@PathVariable Long id);
}
