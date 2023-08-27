package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
    @NotEmpty(message = "User id is mandatory.")
    Long userId,
    @NotEmpty
    @Size(min = 2, max = 64, message = "Display name length should be 2 - 64 characters.")
    String displayName,
    String profilePictureUrl
) {
}
