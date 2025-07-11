package sk.krizan.fitness_app_be.helper;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;

import java.time.Instant;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserHelper {

    private static final Faker faker = new Faker();

    public static User createMockUser(Set<Role> roles) {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.addToRoleSet(roles);
        user.setCreatedAt(Instant.now());
        user.setActive(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setLocked(false);
        user.setPassword("");
        return user;
    }
}
