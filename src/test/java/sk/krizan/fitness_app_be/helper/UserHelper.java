package sk.krizan.fitness_app_be.helper;

import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;

import java.time.Instant;
import java.util.Set;

public class UserHelper {

    public static User createMockUser(String email, Set<Role> roles) {
        User user = new User();
        user.setEmail(email);
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
