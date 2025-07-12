package sk.krizan.fitness_app_be.helper;

import com.github.javafaker.Faker;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.ProfileFilterRequest;
import sk.krizan.fitness_app_be.controller.response.PageResponse;
import sk.krizan.fitness_app_be.controller.response.ProfileResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;

import java.util.Comparator;
import java.util.List;

import static sk.krizan.fitness_app_be.helper.DefaultValues.DEFAULT_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileHelper {

    private static final Faker faker = new Faker();

    public static Profile createMockProfile(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setName(faker.funnyName().name());
        profile.setBio(DEFAULT_VALUE);
        profile.setPreferredWeightUnit(WeightUnit.KG);
        user.setProfile(profile);
        return profile;
    }

    public static ProfileFilterRequest createFilterRequest(
            @NotNull Integer page,
            @NotNull Integer size,
            @NotNull String sortBy,
            @NotNull String sortDirection,
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

    public static void assertFilter(
            List<Profile> expectedList,
            ProfileFilterRequest request,
            PageResponse<ProfileResponse> response
    ) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.pageNumber());
        Assertions.assertNotNull(response.pageSize());
        Assertions.assertNotNull(response.totalElements());
        Assertions.assertNotNull(response.totalPages());
        Assertions.assertNotNull(response.results());
        Assertions.assertFalse(response.results().isEmpty());
        Assertions.assertEquals(request.page(), response.pageNumber());
        Assertions.assertEquals(expectedList.size(), response.results().size());

        List<ProfileResponse> results = response.results();
        results.sort(Comparator.comparingLong(ProfileResponse::id));
        expectedList.sort(Comparator.comparingLong(Profile::getId));
        for (int i = 0; i < results.size(); i++) {
            ProfileResponse profileResponse = results.get(i);
            Profile profile = expectedList.get(i);
            assertGet(profile, profileResponse);
        }
    }

    public static void assertGet(Profile profile, ProfileResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(profile.getId(), response.id());
        Assertions.assertEquals(profile.getUser().getId(), response.userId());
        Assertions.assertEquals(profile.getName(), response.name());
        Assertions.assertEquals(profile.getProfilePictureUrl(), response.profilePictureUrl());
        EnumHelper.assertEnumResponse(profile.getPreferredWeightUnit().getKey(), response.preferredWeightUnitResponse());
    }

    public static void assertDelete(Profile profile, Long deletedProfileId) {
        Assertions.assertEquals(profile.getId(), deletedProfileId);
        Assertions.assertTrue(profile.getDeleted());
    }
}
