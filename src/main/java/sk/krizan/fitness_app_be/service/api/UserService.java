package sk.krizan.fitness_app_be.service.api;

import org.springframework.security.core.userdetails.UserDetailsService;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.model.User;
import sk.krizan.fitness_app_be.model.enums.Role;

public interface UserService extends UserDetailsService {

    User getUserById(Long id);
    User createUser(SignUpRequest request, Role role);
}
