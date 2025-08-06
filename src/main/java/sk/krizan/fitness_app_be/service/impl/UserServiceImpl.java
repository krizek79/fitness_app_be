package sk.krizan.fitness_app_be.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.controller.exception.ApplicationException;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.model.CustomUserDetails;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.UserMapper;
import sk.krizan.fitness_app_be.repository.UserRepository;
import sk.krizan.fitness_app_be.service.api.UserService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .map(CustomUserDetails::new)
            .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "User with email { " + email + " } does not exist."));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "User with id { " + id + " } does not exist."));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "User with email { " + email + " } does not exist."));
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;

        if (principal instanceof Jwt jwt) {
            email = jwt.getSubject();
        } else if (principal instanceof CustomUserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            throw new ApplicationException(HttpStatus.UNAUTHORIZED, "Unrecognized authentication principal");
        }

        return getUserByEmail(email);
    }

    @Override
    @Transactional
    public User createUser(SignUpRequest request, Set<Role> roleSet) {
        Boolean existsByEmail = userRepository.existsByEmail(request.email());
        if (existsByEmail) {
            throw new ApplicationException(HttpStatus.CONFLICT, "User with email { " + request.email() + " } already exists.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = UserMapper.signUpRequestToEntity(request, roleSet, encodedPassword);
        return userRepository.save(user);
    }
}
