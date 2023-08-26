package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LocalAuthenticationRequest(
    @NotEmpty
    @Email
    String email,
    @NotEmpty
    String password
) {
}
