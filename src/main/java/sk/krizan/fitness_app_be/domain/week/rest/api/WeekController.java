package sk.krizan.fitness_app_be.domain.week.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.krizan.fitness_app_be.common.exception.ProblemDetails;
import sk.krizan.fitness_app_be.domain.week.rest.dto.response.WeekDetailResponse;

@Tag(
        name = "Week",
        description = "Manage weeks"
)
@RequestMapping("weeks")
public interface WeekController {

    @Operation(
            summary = "Get week by ID",
            description = "Retrieves a specific week by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Week found",
                            content = @Content(schema = @Schema(implementation = WeekDetailResponse.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Week not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ProblemDetails.class)))
            }
    )
    @GetMapping("{id}")
    WeekDetailResponse getWeekById(@PathVariable Long id);

}
