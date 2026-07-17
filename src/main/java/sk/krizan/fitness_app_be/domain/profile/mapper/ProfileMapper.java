package sk.krizan.fitness_app_be.domain.profile.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileDetailResponse;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.reference.mapper.ReferenceDataMapper;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper {

    public static ProfileDetailResponse entityToResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileDetailResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .publicId(profile.getPublicId())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .preferredWeightUnit(ReferenceDataMapper.enumToResponse(profile.getPreferredWeightUnit()))
                .preferredDistanceUnit(ReferenceDataMapper.enumToResponse(profile.getPreferredDistanceUnit()))
                .build();
    }

    public static ProfileSimpleResponse entityToSimpleResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileSimpleResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .build();
    }

}
