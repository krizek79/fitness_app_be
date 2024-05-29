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
    @Email(message = "Email is not valid.")
    String email,
    @NotEmpty
    @Size(min = 4, message = "Password should have at least 4 characters")
    String password,
    @NotEmpty
    String matchingPassword
) {
}
