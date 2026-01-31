package sk.krizan.fitness_app_be.domain.draft.rest.api;

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
import sk.krizan.fitness_app_be.domain.draft.rest.dto.wrapper.DraftPageResponse;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftCreateRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftFilterRequest;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.request.DraftUpdateRequest;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.domain.draft.rest.dto.response.DraftResponse;

@Tag(
        name = "Draft",
        description = "Manage drafts"
)
@RequestMapping("drafts")
public interface DraftController {

    @Operation(
            summary = "Filter drafts",
            description = "Returns a paginated list of drafts matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Drafts retrieved successfully",
                            content = @Content(schema = @Schema(implementation = DraftPageResponse.class))),
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
    PageResponse<DraftResponse> filterDrafts(@Valid @RequestBody DraftFilterRequest request);

    @Operation(
            summary = "Get draft by ID",
            description = "Retrieves a specific draft by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Draft found",
                            content = @Content(schema = @Schema(implementation = DraftResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Storage draft not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    DraftResponse getDraftById(@PathVariable Long id);

    @Operation(
            summary = "Create a draft",
            description = "Creates a new draft.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Draft created",
                            content = @Content(schema = @Schema(implementation = DraftResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
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
    DraftResponse createDraft(@Valid @RequestBody DraftCreateRequest request);

    @Operation(
            summary = "Update a draft",
            description = "Updates an existing draft.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Draft updated",
                            content = @Content(schema = @Schema(implementation = DraftResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
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
                            description = "Draft not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping("{id}")
    DraftResponse updateDraft(@PathVariable Long id, @Valid @RequestBody DraftUpdateRequest request);

    @Operation(
            summary = "Delete a draft",
            description = "Deletes the draft with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Draft deleted successfully",
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
                            description = "Draft not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDraft(@PathVariable Long id);
}
