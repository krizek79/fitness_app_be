package sk.krizan.fitness_app_be.domain.user.rest.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileDetailResponse;

@Builder
@FieldNameConstants
public record UserResponse(
        Long id,
        String email,
        boolean isAdmin,
        ProfileDetailResponse profile
) {
}
