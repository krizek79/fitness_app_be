package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
    @NotEmpty(message = "User id is mandatory.")
    Long userId,
    @NotEmpty
    @Size(
        min = 2,
        max = 64,
        message = "Display name shouldn't be less than 2 or more than 64 characters long."
    )
    String displayName,
    String profilePictureUrl,
    @Size(max = 128, message = "Bio shouldn't be more than 128 characters long.")
    String bio
) {
}
