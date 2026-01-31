package sk.krizan.fitness_app_be.domain.cycle.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record CycleCreateRequest(
        Long traineeId,
        @NotEmpty
        @Size(max = 255)
        String title
) {
}
