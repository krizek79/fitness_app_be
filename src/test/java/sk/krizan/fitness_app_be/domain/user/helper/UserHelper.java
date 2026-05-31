package sk.krizan.fitness_app_be.domain.user.helper;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.domain.profile.helper.ProfileHelper;
import sk.krizan.fitness_app_be.domain.user.rest.dto.response.UserResponse;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.entity.Role;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserHelper {

    private static final Faker faker = new Faker();

    public static User createUser(Set<Role> roles) {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setKeycloakId(UUID.randomUUID().toString());
        user.addToRoles(roles);
        return user;
    }

    public static void assertUserResponse(User user, UserResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(user.getId(), response.id());
        Assertions.assertEquals(user.getEmail(), response.email());
        Assertions.assertEquals(user.getRoles(), response.roles());
        if (user.getProfile() == null) {
            Assertions.assertNull(response.profileResponse());
        } else {
            ProfileHelper.assertProfileResponse(user.getProfile(), response.profileResponse());
        }
    }

}
