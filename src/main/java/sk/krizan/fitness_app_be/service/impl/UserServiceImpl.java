package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.model.CustomUserDetails;
import sk.krizan.fitness_app_be.model.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .map(CustomUserDetails::new)
            .orElseThrow(() -> new NotFoundException(
                "User with email { " + email + " } does not exist."));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("User with id { " + id + " } does not exist."));
    }

    @Override
    public User createUser(SignUpRequest request, Role role) {
        return null;
    }
}
