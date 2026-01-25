package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper {

    public static ProfileResponse entityToResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .name(profile.getName())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .preferredWeightUnitResponse(EnumMapper.enumToResponse(profile.getPreferredWeightUnit()))
                .build();
    }
}
