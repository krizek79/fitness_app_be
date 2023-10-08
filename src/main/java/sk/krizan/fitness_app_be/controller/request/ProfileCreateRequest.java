package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ProfileCreateRequest(
    @NotEmpty(message = "Name is mandatory.")
    @Size(
        min = 2,
        max = 64,
        message = "Name shouldn't be less than 2 or more than 64 characters long."
    )
    String name,
    String profilePictureUrl,
    @Size(max = 128, message = "Bio shouldn't be more than 128 characters long.")
    String bio
) {
}
