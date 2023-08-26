package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
    @NotEmpty
    Long userId,
    @NotEmpty
    @Size(min = 2, max = 64, message = "Name length should be 2 - 64 characters.")
    String displayName,
    String profilePictureUrl
) {
}
