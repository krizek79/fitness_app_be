package sk.krizan.fitness_app_be.domain.user.service.api;

import org.springframework.security.oauth2.jwt.Jwt;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.entity.Role;

import java.util.Set;

public interface UserService {

    User getUserById(Long id);

    User getCurrentUser();

    User syncUser(Jwt jwt, Set<Role> roles);
}
