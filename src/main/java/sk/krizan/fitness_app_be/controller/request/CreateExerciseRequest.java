package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateExerciseRequest(
    @NotEmpty(message = "Name is mandatory.")
    @Size(
        min = 2,
        max = 64,
        message = "Name shouldn't be less than 2 or more than 64 characters long."
    )
    String name,
    Set<String> muscleGroupKeys
) {
}
