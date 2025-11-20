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
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.GoalPageResponse;
import sk.krizan.fitness_app_be.controller.request.GoalCreateRequest;
import sk.krizan.fitness_app_be.controller.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.controller.request.GoalUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;
import sk.krizan.fitness_app_be.controller.response.GoalResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;

@Tag(
        name = "Goal",
        description = "Manage training goals"
)
@RequestMapping("goals")
public interface GoalController {

    @Operation(
            summary = "Filter goals",
            description = "Returns a paginated list of goals matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Goals retrieved successfully",
                            content = @Content(schema = @Schema(implementation = GoalPageResponse.class))),
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
    PageResponse<GoalResponse> filterGoals(@Valid @RequestBody GoalFilterRequest request);

    @Operation(
            summary = "Get goal by ID",
            description = "Retrieves a specific goal by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Goal found",
                            content = @Content(schema = @Schema(implementation = GoalResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Goal not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    GoalResponse getGoalById(@PathVariable Long id);

    @Operation(
            summary = "Create a new goal",
            description = "Creates a new training goal using the provided input data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Goal created successfully",
                            content = @Content(schema = @Schema(implementation = GoalResponse.class))),
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
    GoalResponse createGoal(@Valid @RequestBody GoalCreateRequest request);

    @Operation(
            summary = "Update a goal",
            description = "Updates a specific goal identified by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Goal updated successfully",
                            content = @Content(schema = @Schema(implementation = GoalResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Goal not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping("{id}")
    GoalResponse updateGoal(@PathVariable Long id, @Valid @RequestBody GoalUpdateRequest request);

    @Operation(
            summary = "Delete a goal",
            description = "Deletes a goal by its ID and returns the ID of the deleted goal.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Goal deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Goal not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteGoal(@PathVariable Long id);

    @Operation(
            summary = "Trigger goal achieved attribute",
            description = "Toggles a goal achieved attribute and returns the updated goal.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Toggles Goal achieved",
                            content = @Content(schema = @Schema(implementation = GoalResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Goal not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PatchMapping("{id}/trigger-achieved")
    GoalResponse triggerAchieved(@PathVariable Long id);
}
