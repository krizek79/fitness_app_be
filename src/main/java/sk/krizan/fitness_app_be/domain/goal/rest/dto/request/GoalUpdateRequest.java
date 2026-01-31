package sk.krizan.fitness_app_be.domain.goal.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record GoalUpdateRequest(
        @NotEmpty
        @Size(max = 1000)
        String text
) {
}
