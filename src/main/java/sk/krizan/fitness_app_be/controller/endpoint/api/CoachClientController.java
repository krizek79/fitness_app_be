package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.CoachClienPageResponse;
import sk.krizan.fitness_app_be.controller.request.CoachClientCreateRequest;
import sk.krizan.fitness_app_be.controller.request.CoachClientFilterRequest;
import sk.krizan.fitness_app_be.controller.response.CoachClientResponse;
import sk.krizan.fitness_app_be.controller.response.ExceptionResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;

@Tag(
        name = "CoachClient",
        description = "Manage coach-client relationships"
)
@RequestMapping("coach-clients")
public interface CoachClientController {

    @Operation(
            summary = "Filter coach-client relationships",
            description = "Returns a paginated list of coach-client relationships that match the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of coach-clients",
                            content = @Content(schema = @Schema(implementation = CoachClienPageResponse.class))),
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
    PageResponse<CoachClientResponse> filterCoachClients(@Valid @RequestBody CoachClientFilterRequest request);

    @Operation(
            summary = "Get coach-client by ID",
            description = "Retrieves a specific coach-client relationship by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Coach-client found",
                            content = @Content(schema = @Schema(implementation = CoachClientResponse.class))),
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
    @GetMapping("{id}")
    CoachClientResponse getCoachClientById(@PathVariable Long id);

    @Operation(
            summary = "Create new coach-client relationship",
            description = "Creates a new relationship between a coach and a client based on the provided input.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Coach-client created successfully",
                            content = @Content(schema = @Schema(implementation = CoachClientResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Coach or Client not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    CoachClientResponse createCoachClient(@Valid @RequestBody CoachClientCreateRequest request);
}

