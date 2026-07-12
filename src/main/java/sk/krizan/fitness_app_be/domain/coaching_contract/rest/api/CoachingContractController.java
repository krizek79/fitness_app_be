package sk.krizan.fitness_app_be.domain.coaching_contract.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.wrapper.CoachingContractClientPageResponse;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.wrapper.CoachingContractCoachPageResponse;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.wrapper.CoachingContractPageResponse;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractCreateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterConnectionsRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractFilterRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.request.CoachingContractStatusUpdateRequest;
import sk.krizan.fitness_app_be.domain.coaching_contract.rest.dto.response.CoachingContractResponse;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;

@Tag(
        name = "CoachingContract",
        description = "Manage coaching contracts"
)
@RequestMapping("coaching-contracts")
public interface CoachingContractController {

    @Operation(
            summary = "Filter coaching contracts",
            description = "Returns a paginated list of coaching contracts that match the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of coaching contracts",
                            content = @Content(schema = @Schema(implementation = CoachingContractPageResponse.class))),
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
    PageResponse<CoachingContractResponse> filterCoachingContracts(@Valid @RequestBody CoachingContractFilterRequest request);

    @Operation(
            summary = "Get coaching contract by ID",
            description = "Retrieves a specific coaching contracts by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Coaching contract found",
                            content = @Content(schema = @Schema(implementation = CoachingContractResponse.class))),
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
    @GetMapping("{id}")
    CoachingContractResponse getCoachingContractById(@PathVariable Long id);

    @Operation(
            summary = "Create new coaching contract",
            description = "Creates a new coaching contract based on the provided input.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Coaching contract created successfully",
                            content = @Content(schema = @Schema(implementation = CoachingContractResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Coach or Client not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CoachingContractResponse createCoachingContract(@Valid @RequestBody CoachingContractCreateRequest request);

    @Operation(
            summary = "Filter clients",
            description = "Returns a paginated list of profiles — the current user's own profile (page 0 only) plus all active clients of the current user as coach.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profiles retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CoachingContractClientPageResponse.class))),
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
    @PostMapping("clients")
    PageResponse<ProfileSimpleResponse> filterClients(@Valid @RequestBody CoachingContractFilterConnectionsRequest request);

    @Operation(
            summary = "Filter coaches",
            description = "Returns a paginated list of profiles — the current user's own profile (page 0 only) plus all active coaches of the current user as client.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profiles retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CoachingContractCoachPageResponse.class))),
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
    @PostMapping("coaches")
    PageResponse<ProfileSimpleResponse> filterCoaches(@Valid @RequestBody CoachingContractFilterConnectionsRequest request);

    @Operation(
            summary = "Transition a coaching contract's status",
            description = "Applies an action (ACCEPT, DENY, CANCEL, TERMINATE) to a coaching contract, moving it to a new status. " +
                    "The action must be valid for the contract's current status and the caller's role (coach vs client) on it.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Coaching contract status updated successfully",
                            content = @Content(schema = @Schema(implementation = CoachingContractResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Coaching contract not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Action not valid for the contract's current status or caller role",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PatchMapping("{id}/status")
    CoachingContractResponse updateStatus(@PathVariable Long id, @Valid @RequestBody CoachingContractStatusUpdateRequest request);

}

