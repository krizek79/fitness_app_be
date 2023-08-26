package sk.krizan.fitness_app_be.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.configuration.jwt.JwtProvider;
import sk.krizan.fitness_app_be.configuration.jwt.JwtValues;
import sk.krizan.fitness_app_be.controller.request.ClientAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.LocalAuthenticationRequest;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;
import sk.krizan.fitness_app_be.model.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.mapper.UserMapper;
import sk.krizan.fitness_app_be.service.api.AuthenticationService;
import sk.krizan.fitness_app_be.service.api.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtValues jwtValues;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse signInLocal(LocalAuthenticationRequest request) {
        User user = userService.getUserByEmail(request.email());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                request.password()
            )
        );

        String token = jwtProvider.generateToken(authentication);

        return AuthenticationResponse.builder()
            .token(token)
            .expiresAt(Instant.now().plus(jwtValues.getExpiration(), ChronoUnit.HOURS))
            .userResponse(UserMapper.userToResponse(user))
            .build();
    }

    @Override
    public AuthenticationResponse signInMeta(ClientAuthenticationRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse signInGoogle(ClientAuthenticationRequest request) {
        return null;
    }

    @Override
    public String signUp(SignUpRequest request) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.LOCAL_USER);

        userService.createUser(request, roles);
        return "Registration successful";
    }
}
