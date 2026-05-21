package sk.krizan.fitness_app_be.domain.profile.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper {

    public static ProfileResponse entityToResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .preferredWeightUnitResponse(ReferenceDataMapper.enumToResponse(profile.getPreferredWeightUnit()))
                .build();
    }

    public static ProfileSimpleResponse entityToSimpleResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileSimpleResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .build();
    }

}
