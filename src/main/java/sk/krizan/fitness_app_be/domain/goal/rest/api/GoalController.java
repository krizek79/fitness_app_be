package sk.krizan.fitness_app_be.domain.goal.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalFilterRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.request.GoalInputRequest;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.response.GoalResponse;
import sk.krizan.fitness_app_be.domain.goal.rest.dto.wrapper.GoalPageResponse;

@Tag(
        name = "Goal",
        description = "Manage goals"
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
            summary = "Create a new goal",
            description = "Creates a new goal based on the provided input data.",
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
    GoalResponse createGoal(@Valid @Validated(CreateGroup.class) @RequestBody GoalInputRequest request);

    @Operation(
            summary = "Update an existing goal",
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
    GoalResponse updateGoal(@PathVariable @Validated(UpdateGroup.class) Long id, @Valid @RequestBody GoalInputRequest request);

    @Operation(
            summary = "Delete a training goal",
            description = "Deletes a training goal by its ID and returns the ID of the deleted goal.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Goal deleted successfully",
                            content = @Content(schema = @Schema())),
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGoal(@PathVariable Long id);

}
