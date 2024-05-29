package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record LocalAuthenticationRequest(
    @NotEmpty
    @Email
    String email,
    String password
) {
}
