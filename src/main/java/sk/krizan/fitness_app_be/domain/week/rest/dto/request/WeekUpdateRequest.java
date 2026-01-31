package sk.krizan.fitness_app_be.domain.week.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

@Builder
@FieldNameConstants
public record WeekUpdateRequest(
        @NotNull
        Long id,
        @NotNull
        @Min(1)
        Integer order,
        @Length(max = 1024)
        String note
) {
}
