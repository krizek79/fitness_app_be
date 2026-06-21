package sk.krizan.fitness_app_be.domain.reference.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.domain.reference.rest.dto.response.ReferenceDataResponse;

import java.util.List;

@Tag(
        name = "ReferenceData",
        description = "Provides available values for reference data (enums)"
)
@RequestMapping("reference-data")
public interface ReferenceDataController {

    @Operation(
            summary = "Get available reference data types",
            description = "Returns a list of all available reference data types.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    ),
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
    @GetMapping
    List<String> getAvailableReferenceTypes();

    @Operation(
            summary = "Get reference data by type",
            description = "Returns a list of desired reference data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReferenceDataResponse.class)))
                    ),
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
    @GetMapping("{type}")
    List<ReferenceDataResponse> getReferenceData(@PathVariable String type);

}
