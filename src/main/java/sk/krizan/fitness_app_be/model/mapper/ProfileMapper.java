package sk.krizan.fitness_app_be.model.mapper;

import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.ProfileCreateRequest;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;

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
            .build();
    }

    public static Profile createRequestToEntity(ProfileCreateRequest request, User user) {
        return Profile.builder()
            .user(user)
            .name(request.name())
            .profilePictureUrl(
                request.profilePictureUrl().isBlank() ? null : request.profilePictureUrl())
            .bio(request.bio().isEmpty() ? "" : request.bio())
            .authoredWorkouts(new ArrayList<>())
            .build();
    }
}
