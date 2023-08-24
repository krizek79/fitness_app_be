package sk.krizan.fitness_app_be.controller.request;

public record LocalAuthenticationRequest(
    String email,
    String password
) {
}
