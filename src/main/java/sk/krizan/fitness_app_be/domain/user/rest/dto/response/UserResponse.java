package sk.krizan.fitness_app_be.domain.user.rest.dto.response;

import java.util.Set;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileResponse;
import sk.krizan.fitness_app_be.domain.user.entity.Role;

@Builder
@FieldNameConstants
public record UserResponse(
        Long id,
        String email,
        Set<Role> roles,
        ProfileResponse profileResponse
) {
}
