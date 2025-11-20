package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.TagPageResponse;
import sk.krizan.fitness_app_be.controller.request.TagCreateRequest;
import sk.krizan.fitness_app_be.controller.request.TagFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.TagResponse;

@Tag(
        name = "Tag",
        description = "Manage tags"
)
@RequestMapping("tags")
public interface TagController {

    @Operation(
            summary = "Filter tags",
            description = "Returns a paginated list of tags matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tags retrieved successfully",
                            content = @Content(schema = @Schema(implementation = TagPageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    )
            }
    )
    @PostMapping("filter")
    PageResponse<TagResponse> filterTags(@Valid @RequestBody TagFilterRequest request);

    @Operation(
            summary = "Create new tag",
            description = "Creates a new tag with the given data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tag created successfully",
                            content = @Content(schema = @Schema(implementation = TagResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TagResponse createTag(@Valid @RequestBody TagCreateRequest request);

    @Operation(
            summary = "Delete tag",
            description = "Deletes a tag by its ID and returns the ID of the deleted tag.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tag deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    )
            }
    )
    @DeleteMapping("{id}")
    Long deleteTag(@PathVariable Long id);
}
