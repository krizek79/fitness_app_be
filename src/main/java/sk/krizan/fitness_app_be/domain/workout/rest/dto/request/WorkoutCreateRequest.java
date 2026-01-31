package sk.krizan.fitness_app_be.domain.workout.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;

import java.util.Set;

@Builder
@FieldNameConstants
public record WorkoutCreateRequest(
        @NotEmpty
        @Size(min = 1, max = 64)
        String title,
        @Size(max = 1000)
        String description,
        @NotNull
        WeightUnit weightUnit,
        @Length(max = 1024)
        String note,
        @NotNull
        Boolean isTemplate,
        Set<String> tagNames,
        Long traineeId
) {
}
