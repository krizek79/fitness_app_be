package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.krizan.fitness_app_be.controller.endpoint.api.dto_wrapper.WeekWorkoutPageResponse;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutFilterRequest;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutUpdateRequest;
import sk.krizan.fitness_app_be.controller.response.ExceptionResponse;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;

@Tag(
        name = "WeekWorkout",
        description = "Manage week workouts"
)
@RequestMapping("week-workouts")
public interface WeekWorkoutController {

    @Operation(
            summary = "Filter week workouts",
            description = "Returns a paginated list of week workouts matching the provided filter criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully filtered week workouts",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutPageResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
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
    @PostMapping("filter")
    PageResponse<WeekWorkoutResponse> filterWeekWorkouts(@Valid @RequestBody WeekWorkoutFilterRequest request);

    @Operation(
            summary = "Get a week workout by ID",
            description = "Retrieves a single week workout based on its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week workout found",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week workout not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("{id}")
    WeekWorkoutResponse getWeekWorkoutById(@PathVariable Long id);

    @Operation(
            summary = "Create a new week workout",
            description = "Creates a new week workout entity with the given request data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Week workout created successfully",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    WeekWorkoutResponse createWeekWorkout(@Valid @RequestBody WeekWorkoutCreateRequest request);

    @Operation(
            summary = "Update a week workout",
            description = "Updates an existing week workout entity based on its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week workout updated successfully",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week workout not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping("{id}")
    WeekWorkoutResponse updateWeekWorkout(@PathVariable Long id, @Valid @RequestBody WeekWorkoutUpdateRequest request);

    @Operation(
            summary = "Delete a week workout",
            description = "Deletes the week workout with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week workout deleted successfully",
                            content = @Content(schema = @Schema(implementation = Long.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week workout not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @DeleteMapping("{id}")
    Long deleteWeekWorkout(@PathVariable Long id);

    @Operation(
            summary = "Trigger completed state of a week workout",
            description = "Toggles the 'completed' state of the week workout with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week workout completed state toggled successfully",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week workout not found",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PatchMapping("{id}/trigger-completed")
    WeekWorkoutResponse triggerCompleted(@PathVariable Long id);
}
