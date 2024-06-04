package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProfileUpdateRequest(
    @NotEmpty
    @Size(min = 2, max = 64)
    String name,
    String profilePictureUrl,
    @Size(max = 128)
    String bio
) {
}
