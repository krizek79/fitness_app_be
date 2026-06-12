package sk.krizan.fitness_app_be.domain.exercise.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.common.validation.group.UpdateGroup;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseFilterRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.request.ExerciseInputRequest;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseDetailResponse;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.response.ExerciseSimpleResponse;
import sk.krizan.fitness_app_be.domain.exercise.rest.dto.wrapper.ExercisePageResponse;

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
    PageResponse<ExerciseSimpleResponse> filterExercises(@Valid @RequestBody ExerciseFilterRequest request);

    @Operation(
            summary = "Get exercise by ID",
            description = "Retrieves a specific exercise by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exercise found",
                            content = @Content(schema = @Schema(implementation = ExerciseDetailResponse.class))),
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
    ExerciseDetailResponse getExerciseById(@PathVariable Long id);

    @Operation(
            summary = "Create a new exercise",
            description = "Creates a new exercise using the provided input data and optional thumbnail image.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Exercise created successfully",
                            content = @Content(schema = @Schema(implementation = ExerciseDetailResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid file upload",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "413",
                            description = "File too large",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported media type",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ExerciseDetailResponse createExercise(
            @Valid @RequestPart @Validated(value = CreateGroup.class) ExerciseInputRequest request,
            @RequestPart(required = false) MultipartFile thumbnail
    );

    @Operation(
            summary = "Update an existing exercise",
            description = "Updates an existing exercise identified by its ID with the provided input data and optional thumbnail image.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exercise updated successfully",
                            content = @Content(schema = @Schema(implementation = ExerciseDetailResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid file upload",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Exercise not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "413",
                            description = "File too large",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported media type",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ExerciseDetailResponse updateExercise(
            @PathVariable Long id,
            @Valid @RequestPart @Validated(value = UpdateGroup.class) ExerciseInputRequest request,
            @RequestPart(required = false) MultipartFile thumbnail
    );

    @Operation(
            summary = "Delete an exercise",
            description = "Deletes an exercise by its ID and returns the ID of the deleted exercise.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Exercise deleted successfully",
                            content = @Content(schema = @Schema())),
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteExercise(@PathVariable Long id);

}

