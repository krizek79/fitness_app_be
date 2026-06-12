package sk.krizan.fitness_app_be.domain.equipment.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.common.validation.group.UpdateGroup;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentFilterRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.request.EquipmentInputRequest;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.response.EquipmentResponse;
import sk.krizan.fitness_app_be.domain.equipment.rest.dto.wrapper.EquipmentPageResponse;

@Tag(
        name = "Equipment",
        description = "Manage requiredEquipment"
)
@RequestMapping("equipment")
public interface EquipmentController {

    @Operation(
            summary = "Filter equipment",
            description = "Returns a paginated list of equipment matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Equipment retrieved successfully",
                            content = @Content(schema = @Schema(implementation = EquipmentPageResponse.class))),
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
    PageResponse<EquipmentResponse> filterEquipment(@Valid @RequestBody EquipmentFilterRequest request);

    @Operation(
            summary = "Create a new equipment",
            description = "Creates a new equipment based on the provided input data and optional thumbnail image.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Equipment created successfully",
                            content = @Content(schema = @Schema(implementation = EquipmentResponse.class))),
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
    EquipmentResponse createEquipment(
            @Valid @RequestPart @Validated(CreateGroup.class) EquipmentInputRequest request,
            @RequestPart(required = false) MultipartFile thumbnail
    );

    @Operation(
            summary = "Update an existing equipment",
            description = "Updates a specific equipment identified by its ID with the provided input data and optional thumbnail image.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Equipment updated successfully",
                            content = @Content(schema = @Schema(implementation = EquipmentResponse.class))),
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
                            description = "Equipment not found",
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
    EquipmentResponse updateEquipment(
            @PathVariable Long id,
            @Valid @RequestPart @Validated(UpdateGroup.class) EquipmentInputRequest request,
            @RequestPart(required = false) MultipartFile thumbnail
    );

    @Operation(
            summary = "Delete equipment",
            description = "Deletes equipment by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Equipment deleted successfully",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Equipment not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteEquipment(@PathVariable Long id);

}
