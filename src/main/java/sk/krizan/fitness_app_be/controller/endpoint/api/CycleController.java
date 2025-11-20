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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.CyclePageResponse;
import sk.krizan.fitness_app_be.controller.request.CycleCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CycleFilterRequest;
import sk.krizan.fitness_app_be.controller.request.CycleUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;
import sk.krizan.fitness_app_be.controller.response.PageResponse;

@Tag(
        name = "Cycle",
        description = "Manage training cycles"
)
@RequestMapping("cycles")
public interface CycleController {

    @Operation(
            summary = "Filter training cycles",
            description = "Returns a paginated list of training cycles that match the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of cycles",
                            content = @Content(schema = @Schema(implementation = CyclePageResponse.class))),
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
    PageResponse<CycleResponse> filterCycles(@Valid @RequestBody CycleFilterRequest request);

    @Operation(
            summary = "Get cycle by ID",
            description = "Retrieves a specific training cycle by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cycle found",
                            content = @Content(schema = @Schema(implementation = CycleResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cycle not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    CycleResponse getCycleById(@PathVariable Long id);

    @Operation(
            summary = "Create a new training cycle",
            description = "Creates a new training cycle based on the provided input data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cycle created successfully",
                            content = @Content(schema = @Schema(implementation = CycleResponse.class))),
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
    CycleResponse createCycle(@Valid @RequestBody CycleCreateRequest request);

    @Operation(
            summary = "Update an existing training cycle",
            description = "Updates a specific training cycle identified by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cycle updated successfully",
                            content = @Content(schema = @Schema(implementation = CycleResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cycle not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping("{id}")
    CycleResponse updateCycle(@PathVariable Long id, @Valid @RequestBody CycleUpdateRequest request);

    @Operation(
            summary = "Delete a training cycle",
            description = "Deletes a training cycle by its ID and returns the ID of the deleted cycle.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cycle deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cycle not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteCycle(@PathVariable Long id);
}

