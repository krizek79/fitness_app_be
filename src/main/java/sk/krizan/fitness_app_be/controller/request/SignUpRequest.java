package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import sk.krizan.fitness_app_be.validation.ValueMatching;

@ValueMatching(
    value = "password",
    matchingValue = "matchingPassword"
)
public record SignUpRequest(
    @NotEmpty(message = "Email is mandatory.")
    @Email(message = "Email is not valid.")
    String email,
    @NotEmpty(message = "Password is mandatory.")
    @Size(min = 4, message = "Password should have at least 4 characters")
    String password,
    @NotEmpty(message = "Matching password is mandatory.")
    String matchingPassword,
    @Valid
    CreateProfileRequest createProfileRequest
) {
}
