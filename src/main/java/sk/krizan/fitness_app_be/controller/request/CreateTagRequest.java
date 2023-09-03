package sk.krizan.fitness_app_be.controller.request;

import jakarta.validation.constraints.NotEmpty;

public record CreateTagRequest(
    @NotEmpty(message = "Name is mandatory.")
    String name
) {
}
