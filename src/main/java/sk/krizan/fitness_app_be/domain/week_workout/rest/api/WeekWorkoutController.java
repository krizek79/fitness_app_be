package sk.krizan.fitness_app_be.domain.week_workout.rest.api;

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
import sk.krizan.fitness_app_be.common.validation.group.CreateGroup;
import sk.krizan.fitness_app_be.common.validation.group.UpdateGroup;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.request.WeekWorkoutInputRequest;
import sk.krizan.fitness_app_be.domain.week_workout.rest.dto.response.WeekWorkoutResponse;

@Tag(
        name = "WeekWorkout",
        description = "Manage week workouts"
)
@RequestMapping("week-workouts")
public interface WeekWorkoutController {

    @Operation(
            summary = "Create week workout",
            description = "Creates a new week workout with its child entities.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Week workout created successfully",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    WeekWorkoutResponse createWeekWorkout(@Valid @Validated(CreateGroup.class) @RequestBody WeekWorkoutInputRequest request);

    @Operation(
            summary = "Update week workout",
            description = "Updates the week workout with the specified ID, including workout and its child entities.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week workout updated successfully",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week workout not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @PutMapping("{id}")
    WeekWorkoutResponse updateWeekWorkout(@PathVariable Long id, @Valid @Validated(UpdateGroup.class) @RequestBody WeekWorkoutInputRequest request);

    @Operation(
            summary = "Delete week workout",
            description = "Deletes the workout with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week workout deleted successfully",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteWeekWorkout(@PathVariable Long id);

}
