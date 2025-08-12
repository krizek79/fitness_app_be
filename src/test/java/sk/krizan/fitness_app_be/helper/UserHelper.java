package sk.krizan.fitness_app_be.helper;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sk.krizan.fitness_app_be.controller.response.UserResponse;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;

import java.time.Instant;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserHelper {

    private static final Faker faker = new Faker();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User createMockUser(Set<Role> roles) {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.addToRoleSet(roles);
        user.setCreatedAt(Instant.now());
        user.setActive(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setLocked(false);
        user.setPassword(passwordEncoder.encode(""));
        return user;
    }

    public static void assertUserResponse(User user, UserResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(user.getId(), response.id());
        Assertions.assertEquals(user.getEmail(), response.email());
        Assertions.assertEquals(user.getRoleSet(), response.roles());
        if (user.getProfile() == null) {
            Assertions.assertNull(response.profileResponse());
        } else {
            ProfileHelper.assertGet(user.getProfile(), response.profileResponse());
        }
    }

}
