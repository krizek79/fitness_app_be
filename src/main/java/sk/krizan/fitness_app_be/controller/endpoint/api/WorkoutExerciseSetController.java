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
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.WorkoutExerciseSetPageResponse;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.WorkoutExerciseSetSimpleListResponse;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WorkoutExerciseSetUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.ExceptionResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WorkoutExerciseSetResponse;

@Tag(
        name = "WorkoutExerciseSet",
        description = "Manage workout exercise sets"
)
@RequestMapping("workout-exercise-sets")
public interface WorkoutExerciseSetController {

    @Operation(
            summary = "Filter workout exercise sets",
            description = "Returns a paginated list of workout exercise sets matching the filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Filtered workout exercise sets filtered successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseSetPageResponse.class))),
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
    PageResponse<WorkoutExerciseSetResponse> filterWorkoutExerciseSets(@Valid @RequestBody WorkoutExerciseSetFilterRequest request);

    @Operation(
            summary = "Get workout exercise set by ID",
            description = "Returns the workout exercise set with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout exercise set returned successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseSetResponse.class))),
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
                            description = "Not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("{id}")
    WorkoutExerciseSetResponse getWorkoutExerciseSetById(@PathVariable Long id);

    @Operation(
            summary = "Create workout exercise set",
            description = "Creates a new workout exercise set from the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseSetResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation failed",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
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
    WorkoutExerciseSetResponse createWorkoutExerciseSet(@Valid @RequestBody WorkoutExerciseSetCreateRequest request);

    @Operation(
            summary = "Update workout exercise set",
            description = """
                    Updates an existing workout exercise set with the provided data.
                    
                    ⚠️ **Deprecated:** This endpoint should not be used for bulk updates.
                    For batch updates, please use the `batchUpdateWorkoutExerciseSets` endpoint instead.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseSetResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation failed",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
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
                            description = "Not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping
    WorkoutExerciseSetResponse updateWorkoutExerciseSet(@Valid @RequestBody WorkoutExerciseSetUpdateRequest request);

    @Operation(
            summary = "Batch update workout exercise sets",
            description = "Updates multiple workout exercise sets in a single request.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Batch update successful",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseSetSimpleListResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation failed",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
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
    SimpleListResponse<WorkoutExerciseSetResponse> batchUpdateWorkoutExerciseSets(@Valid @RequestBody BatchUpdateRequest<WorkoutExerciseSetUpdateRequest> request);

    @Operation(
            summary = "Delete workout exercise set",
            description = "Deletes the workout exercise set with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted successfully",
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
                            description = "Not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteWorkoutExerciseSet(@PathVariable Long id);

    @Operation(
            summary = "Trigger completed state of a workout exercise set",
            description = "Toggles the 'completed' state of the workout exercise set with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Completed state toggled successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutExerciseSetResponse.class))),
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
                            description = "Not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PatchMapping("{id}/trigger-completed")
    WorkoutExerciseSetResponse triggerCompleted(@PathVariable Long id);
}
