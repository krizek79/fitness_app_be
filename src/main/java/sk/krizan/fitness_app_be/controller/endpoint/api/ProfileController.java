package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.ProfilePageResponse;
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;

@Tag(
        name = "Profile",
        description = "Manage user profiles"
)
@RequestMapping("profiles")
public interface ProfileController {

    @Operation(
            summary = "Filter profiles",
            description = "Returns a paginated list of user profiles matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profiles retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ProfilePageResponse.class))),
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
    PageResponse<ProfileResponse> filterProfiles(@Valid @RequestBody ProfileFilterRequest request);

    @Operation(
            summary = "Get profile by ID",
            description = "Retrieves a specific profile by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile found",
                            content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Profile not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    ProfileResponse getProfileById(@PathVariable Long id);

    @Operation(
            summary = "Delete profile",
            description = "Deletes a profile by its ID and returns the ID of the deleted profile.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Profile not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteProfile(@PathVariable Long id);

    @Operation(
            summary = "Upload profile picture",
            description = "Uploads a new profile picture and returns the file url.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile picture uploaded successfully",
                            content = @Content(schema = @Schema(implementation = String.class))),
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
    @PostMapping(
            value = "profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    String uploadProfilePicture(@RequestParam @NotNull MultipartFile multipartFile);
}
