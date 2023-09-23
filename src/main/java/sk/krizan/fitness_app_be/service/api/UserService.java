package sk.krizan.fitness_app_be.service.api;

import java.util.Set;
import org.springframework.security.core.userdetails.UserDetailsService;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;

public interface UserService extends UserDetailsService {

    User getUserById(Long id);
    User getUserByEmail(String email);
    User getCurrentUser();
    User createUser(SignUpRequest request, Set<Role> roles);
}
