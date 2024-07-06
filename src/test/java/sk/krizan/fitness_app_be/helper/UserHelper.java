package sk.krizan.fitness_app_be.helper;

import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;

import java.time.Instant;
import java.util.Set;

public class UserHelper {

    public static User createMockUser(String email, Set<Role> roles) {
        return User.builder()
                .id(1L)
                .email(email)
                .roles(roles)
                .createdAt(Instant.now())
                .active(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .locked(false)
                .password("")
                .build();
    }
}
