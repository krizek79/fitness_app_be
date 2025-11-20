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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.ExercisePageResponse;
import sk.krizan.fitness_app_be.controller.request.ExerciseCreateRequest;
import sk.krizan.fitness_app_be.controller.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;
import sk.krizan.fitness_app_be.controller.response.ExerciseResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;

@Tag(
        name = "Exercise",
        description = "Manage exercises"
)
@RequestMapping("exercises")
public interface ExerciseController {

    @Operation(
            summary = "Filter exercises",
            description = "Returns a paginated list of exercises matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exercises retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ExercisePageResponse.class))),
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
    PageResponse<ExerciseResponse> filterExercises(@Valid @RequestBody ExerciseFilterRequest request);

    @Operation(
            summary = "Get exercise by ID",
            description = "Retrieves a specific exercise by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exercise found",
                            content = @Content(schema = @Schema(implementation = ExerciseResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Exercise not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    ExerciseResponse getExerciseById(@PathVariable Long id);

    @Operation(
            summary = "Create a new exercise",
            description = "Creates a new exercise using the provided input data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Exercise created successfully",
                            content = @Content(schema = @Schema(implementation = ExerciseResponse.class))),
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
    ExerciseResponse createExercise(@Valid @RequestBody ExerciseCreateRequest request);

    @Operation(
            summary = "Delete an exercise",
            description = "Deletes an exercise by its ID and returns the ID of the deleted exercise.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exercise deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Exercise not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteExercise(@PathVariable Long id);
}

