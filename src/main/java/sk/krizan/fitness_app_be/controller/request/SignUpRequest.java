package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import sk.krizan.fitness_app_be.validation.ValueMatching;

@Builder
@ValueMatching(
    value = "password",
    matchingValue = "matchingPassword"
)
public record SignUpRequest(
    @NotEmpty
    @Email
    String email,
    @NotEmpty
    @Size(min = 4)
    String password,
    @NotEmpty
    String matchingPassword
) {
}
