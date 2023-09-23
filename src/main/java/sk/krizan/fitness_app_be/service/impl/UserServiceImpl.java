package sk.krizan.fitness_app_be.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.controller.exception.IllegalOperationException;
import sk.krizan.fitness_app_be.controller.exception.NotFoundException;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.model.CustomUserDetails;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.UserMapper;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
            () -> new NotFoundException("User with email { " + email + " } does not exist."));
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;

        if (principal instanceof Jwt) {
            email = ((Jwt) principal).getSubject();
        }

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            email = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        }

        return getUserByEmail(email);
    }

    @Override
    public User createUser(SignUpRequest request, Set<Role> roles) {
        Boolean existsByEmail = userRepository.existsByEmail(request.email());
        if (existsByEmail) {
            throw new IllegalOperationException(
                "User with email { " + request.email() + " } already exists.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = UserMapper.signUpRequestToUser(request, roles, encodedPassword);
        return userRepository.save(user);
    }
}
