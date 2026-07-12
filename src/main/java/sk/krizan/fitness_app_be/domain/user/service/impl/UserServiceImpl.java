package sk.krizan.fitness_app_be.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProvisioningService userProvisioningService;

    @Override
    public User getOrCreateCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            Jwt jwt = jwtToken.getToken();
            String keycloakId = jwt.getSubject();

            return userRepository.findByKeycloakId(keycloakId)
                    .orElseGet(() -> createUserSafely(jwt, keycloakId));
        }

        throw new ApplicationException(HttpStatus.UNAUTHORIZED, "Unrecognized authentication principal");
    }

    @Override
    public boolean isUserAdmin(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + Role.ADMIN.name()));
    }

    private User createUserSafely(Jwt jwt, String keycloakId) {
        try {
            log.info("Inserting new user into database from JWT token with keycloakId: { {} }", keycloakId);
            return userProvisioningService.createNewUserWithProfileFromToken(jwt, keycloakId);
        } catch (DataIntegrityViolationException e) {
            // A concurrent request already inserted this user between our lookup and insert attempt.
            return userRepository.findByKeycloakId(keycloakId).orElseThrow(() -> e);
        }
    }

}
