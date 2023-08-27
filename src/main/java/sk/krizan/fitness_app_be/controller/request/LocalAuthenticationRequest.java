package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LocalAuthenticationRequest(
    @NotEmpty(message = "Email is mandatory.")
    @Email(message = "Email is not valid.")
    String email,
    String password
) {
}
