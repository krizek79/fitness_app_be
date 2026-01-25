package sk.krizan.fitness_app_be.service.api;

import org.springframework.security.oauth2.jwt.Jwt;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;

import java.util.Set;

public interface UserService {

    User getUserById(Long id);

    User getCurrentUser();

    User syncUser(Jwt jwt, Set<Role> roles);
}
