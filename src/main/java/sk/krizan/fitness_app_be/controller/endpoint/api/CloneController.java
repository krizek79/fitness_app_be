package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import sk.krizan.fitness_app_be.controller.request.WeekWorkoutCreateRequest;
import sk.krizan.fitness_app_be.controller.response.CycleResponse;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;
import sk.krizan.fitness_app_be.controller.response.WeekWorkoutResponse;

@Tag(
        name = "Clone",
        description = "Endpoints for cloning/copying of entities"
)
@RequestMapping("clone")
public interface CloneController {

    @Operation(
            summary = "Clone cycle by ID",
            description = "Creates a new cycle by cloning an existing one identified by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cycle successfully cloned",
                            content = @Content(schema = @Schema(implementation = CycleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cycle not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    )
            }
    )
    @PostMapping("cycle/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    CycleResponse cloneCycle(@PathVariable Long id);

    @Operation(
            summary = "Clone workout to week workout",
            description = "Clones an existing workout and creates a new WeekWorkout instance from it.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Workout successfully cloned to WeekWorkout",
                            content = @Content(schema = @Schema(implementation = WeekWorkoutResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))
                    )
            }
    )
    @PostMapping("workout-to-week-workout")
    @ResponseStatus(HttpStatus.CREATED)
    WeekWorkoutResponse cloneWorkoutToWeekWorkout(@Valid @RequestBody WeekWorkoutCreateRequest request);
}
