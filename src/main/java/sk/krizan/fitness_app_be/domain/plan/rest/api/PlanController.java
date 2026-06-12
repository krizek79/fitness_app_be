package sk.krizan.fitness_app_be.domain.plan.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.common.rest.dto.response.PageResponse;
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.common.validation.group.UpdateGroup;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanFilterRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.request.PlanInputRequest;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanDetailResponse;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.response.PlanSimpleResponse;
import sk.krizan.fitness_app_be.domain.plan.rest.dto.wrapper.PlanPageResponse;

@Tag(
        name = "Plan",
        description = "Manage training plans"
)
@RequestMapping("plans")
public interface PlanController {

    @Operation(
            summary = "Filter training plans",
            description = "Returns a paginated list of training plans that match the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of plans",
                            content = @Content(schema = @Schema(implementation = PlanPageResponse.class))),
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
    PageResponse<PlanSimpleResponse> filterPlans(@Valid @RequestBody PlanFilterRequest request);

    @Operation(
            summary = "Get plan by ID",
            description = "Retrieves a specific training plan by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Plan found",
                            content = @Content(schema = @Schema(implementation = PlanDetailResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Plan not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    PlanDetailResponse getPlanById(@PathVariable Long id);

    @Operation(
            summary = "Create a new training plan",
            description = "Creates a new training plan based on the provided input data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Plan created successfully",
                            content = @Content(schema = @Schema(implementation = PlanDetailResponse.class))),
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
    PlanDetailResponse createPlan(@Valid @Validated(CreateGroup.class) @RequestBody PlanInputRequest request);

    @Operation(
            summary = "Update an existing training plan",
            description = "Updates a specific training plan identified by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Plan updated successfully",
                            content = @Content(schema = @Schema(implementation = PlanDetailResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Plan not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping("{id}")
    PlanDetailResponse updatePlan(@PathVariable Long id, @Valid @Validated(UpdateGroup.class) @RequestBody PlanInputRequest request);

    @Operation(
            summary = "Delete a training plan",
            description = "Deletes a training plan by its ID and returns the ID of the deleted plan.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Plan deleted successfully",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Plan not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlan(@PathVariable Long id);

}

