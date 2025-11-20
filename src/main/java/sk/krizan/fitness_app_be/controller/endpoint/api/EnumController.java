package sk.krizan.fitness_app_be.controller.endpoint.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.krizan.fitness_app_be.controller.response.EnumResponse;
import sk.krizan.fitness_app_be.controller.response.ProblemDetails;

import java.util.List;

@Tag(
        name = "Enum",
        description = "Provides available values for enums"
)
@RequestMapping("enums")
public interface EnumController {

    @Operation(
            summary = "Get workout levels",
            description = "Returns a list of possible workout levels.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of workout levels",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnumResponse.class)))
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
    @GetMapping("workout-levels")
    List<EnumResponse> getWorkoutLevels();

    @Operation(
            summary = "Get muscle groups",
            description = "Returns a list of available muscle groups.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of muscle groups",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnumResponse.class)))
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
    @GetMapping("muscle-groups")
    List<EnumResponse> getMuscleGroups();

    @Operation(
            summary = "Get weight units",
            description = "Returns a list of supported weight units.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of weight units",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnumResponse.class)))
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
    @GetMapping("weight-units")
    List<EnumResponse> getWeightUnits();

    @Operation(
            summary = "Get workout exercise types",
            description = "Returns a list of types of workout exercises.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of workout exercise types.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnumResponse.class)))
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
    @GetMapping("workout-exercise-types")
    List<EnumResponse> getWorkoutExerciseTypes();

    @Operation(
            summary = "Get workout exercise set types",
            description = "Returns a list of set types for workout exercises.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of workout exercise set types",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnumResponse.class)))
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
    @GetMapping("workout-exercise-set-types")
    List<EnumResponse> getWorkoutExerciseSetTypes();
}
