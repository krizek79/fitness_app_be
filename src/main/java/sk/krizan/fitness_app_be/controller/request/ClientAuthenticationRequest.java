package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;

public record ClientAuthenticationRequest(
    @NotEmpty(message = "Access token is mandatory.")
    String accessToken
) {
}
