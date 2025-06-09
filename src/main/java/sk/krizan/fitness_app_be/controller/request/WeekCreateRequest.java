package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record WeekCreateRequest(
        @NotNull
        Long cycleId,
        @Min(1)
        @NotNull
        Integer order,
        @Length(max = 1024)
        String note
) {
}
