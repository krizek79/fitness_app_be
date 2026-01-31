package sk.krizan.fitness_app_be.domain.cycle.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.cycle.entity.Level;

@Builder
@FieldNameConstants
public record CycleUpdateRequest(
        @NotEmpty
        @Size(max = 255)
        String title,
        @Size(max = 2000)
        String description,
        @NotNull
        Level level,
        Long traineeId
) {
}
