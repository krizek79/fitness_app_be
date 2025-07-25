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
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.WeekPageResponse;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.WeekSimpleListResponse;
import sk.krizan.fitness_app_be.controller.request.BatchUpdateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.ExceptionResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.SimpleListResponse;
import sk.krizan.fitness_app_be.controller.response.WeekResponse;

@Tag(
        name = "Week",
        description = "Manage weeks"
)
@RequestMapping("weeks")
public interface WeekController {

    @Operation(
            summary = "Filter weeks",
            description = "Returns a paginated list of weeks matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Weeks retrieved successfully",
                            content = @Content(schema = @Schema(implementation = WeekPageResponse.class))),
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
    PageResponse<WeekResponse> filterWeeks(@Valid @RequestBody WeekFilterRequest request);

    @Operation(
            summary = "Get week by ID",
            description = "Retrieves a specific week by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week found",
                            content = @Content(schema = @Schema(implementation = WeekResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("{id}")
    WeekResponse getWeekById(@PathVariable Long id);

    @Operation(
            summary = "Create new week",
            description = "Creates a new week with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Week created successfully",
                            content = @Content(schema = @Schema(implementation = WeekResponse.class))),
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
    WeekResponse createWeek(@Valid @RequestBody WeekCreateRequest request);

    @Operation(
            summary = "Update week",
            description = """
                    Updates an existing week with the provided data.
                    
                    ⚠️ **Deprecated:** This endpoint should not be used for bulk updates.
                    For batch updates, please use the `batchUpdateWeeks` endpoint instead.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week updated successfully",
                            content = @Content(schema = @Schema(implementation = WeekResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping
    WeekResponse updateWeek(@Valid @RequestBody WeekUpdateRequest request);

    @Operation(
            summary = "Batch update weeks",
            description = "Batch updates multiple weeks with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Weeks batch updated successfully",
                            content = @Content(schema = @Schema(implementation = WeekSimpleListResponse.class))),
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
    SimpleListResponse<WeekResponse> batchUpdateWeeks(@Valid @RequestBody BatchUpdateRequest<WeekUpdateRequest> request);

    @Operation(
            summary = "Delete week",
            description = "Deletes a week by its ID and returns the ID of the deleted week.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteWeek(@PathVariable Long id);

    @Operation(
            summary = "Toggle completed status of week",
            description = "Toggles the completed status of the week identified by the given ID. The method flips the current completed state to its opposite.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week completed status toggled successfully",
                            content = @Content(schema = @Schema(implementation = WeekResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PatchMapping("{id}/trigger-completed")
    WeekResponse triggerCompleted(@PathVariable Long id);
}
