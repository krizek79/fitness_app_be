package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import sk.krizan.fitness_app_be.validation.PasswordMatching;

@PasswordMatching(
    password = "password",
    matchingPassword = "matchingPassword"
)
public record SignUpRequest(
    @NotEmpty
    @Email
    String email,
    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String password,
    @NotEmpty
    String matchingPassword
) {
}
