package sk.krizan.fitness_app_be.controller.response;

import java.util.Set;
import lombok.Builder;
import sk.krizan.fitness_app_be.model.enums.Role;

@Builder
public record UserResponse(
    Long id,
    String email,
    Set<Role> roles,
    ProfileResponse profileResponse
) {
}
