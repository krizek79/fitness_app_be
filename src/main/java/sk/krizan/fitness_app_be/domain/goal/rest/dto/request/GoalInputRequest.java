package sk.krizan.fitness_app_be.domain.goal.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record GoalInputRequest(
        @Schema(description = "Should be set only for update. For new object, this field should be null.")
        Long id,

        @NotEmpty
        @Size(max = 1000)
        String text,

        @NotNull
        Boolean achieved
) {
}
