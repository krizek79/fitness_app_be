package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;

import java.util.Set;

@Builder
public record WorkoutUpdateRequest(
        @NotEmpty
        @Size(min = 1, max = 64)
        String name,
        @Size(max = 1000)
        String description,
        @NotEmpty
        WeightUnit weightUnit,
        @Length(max = 1024)
        String note,
        Set<String> tagNames,
        Long traineeId
) {
}
