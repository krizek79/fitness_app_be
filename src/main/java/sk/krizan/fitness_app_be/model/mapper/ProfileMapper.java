package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.CreateProfileRequest;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.Profile;
import sk.krizan.fitness_app_be.model.User;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper {

    public static ProfileResponse profileToResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileResponse.builder()
            .id(profile.getId())
            .userId(profile.getUser().getId())
            .displayName(profile.getDisplayName())
            .profilePictureUrl(profile.getProfilePictureUrl())
            .build();
    }

    public static Profile createProfileRequestToProfile(CreateProfileRequest request, User user) {
        return Profile.builder()
            .user(user)
            .displayName(request.displayName())
            .profilePictureUrl(
                request.profilePictureUrl().isBlank() ? null : request.profilePictureUrl())
            .build();
    }
}
