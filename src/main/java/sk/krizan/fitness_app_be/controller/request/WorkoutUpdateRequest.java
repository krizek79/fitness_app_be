package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Builder
public record WorkoutUpdateRequest(
        @NotEmpty
        String name,
        @Size(max = 1000)
        String description,
        String weightUnitKey,
        Set<String> tagNames,
        @Length(max = 1024)
        String note
) {
}
