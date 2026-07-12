package sk.krizan.fitness_app_be.domain.profile.helper;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileDetailResponse;
import sk.krizan.fitness_app_be.domain.profile.rest.dto.response.ProfileSimpleResponse;
import sk.krizan.fitness_app_be.domain.reference.entity.DistanceUnit;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.reference_data.helper.ReferenceDataHelper;
import sk.krizan.fitness_app_be.domain.user.entity.User;

import static sk.krizan.fitness_app_be.common.util.DefaultValues.DEFAULT_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileHelper {

    private static final Faker faker = new Faker();

    public static Profile createProfile(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setName(faker.funnyName().name());
        profile.setBio(DEFAULT_VALUE);
        profile.setPreferredWeightUnit(WeightUnit.KG);
        profile.setPreferredDistanceUnit(DistanceUnit.KM);
        user.setProfile(profile);
        return profile;
    }

    public static ProfileFilterRequest createFilterRequest(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            String name
    ) {
        return ProfileFilterRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .name(name)
                .build();
    }

    public static void assertProfileResponse(Profile profile, ProfileDetailResponse response) {
        if (profile == null) {
            return;
        }

        Assertions.assertNotNull(response);
        Assertions.assertEquals(profile.getId(), response.id());
        Assertions.assertEquals(profile.getName(), response.name());
        Assertions.assertEquals(profile.getProfilePictureUrl(), response.profilePictureUrl());
        ReferenceDataHelper.assertReferenceDataResponse(profile.getPreferredWeightUnit(), response.preferredWeightUnit());
        ReferenceDataHelper.assertReferenceDataResponse(profile.getPreferredDistanceUnit(), response.preferredDistanceUnit());
    }

    public static void assertProfileSimpleResponse(Profile profile, ProfileSimpleResponse response) {
        if (profile == null) {
            return;
        }

        Assertions.assertNotNull(response);
        Assertions.assertEquals(profile.getId(), response.id());
        Assertions.assertEquals(profile.getName(), response.name());
        Assertions.assertEquals(profile.getProfilePictureUrl(), response.profilePictureUrl());
    }

}
