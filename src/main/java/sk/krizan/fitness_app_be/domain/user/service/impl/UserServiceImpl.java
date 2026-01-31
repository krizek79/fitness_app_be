package sk.krizan.fitness_app_be.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.common.exception.ApplicationException;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "User with id { " + id + " } does not exist."));
    }

    @Override
    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            Jwt jwt = jwtToken.getToken();
            String keycloakId = jwt.getSubject();

            return userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "User with keycloakId { " + keycloakId + " } does not exist."));
        }

        throw new ApplicationException(HttpStatus.UNAUTHORIZED, "Unrecognized authentication principal");
    }

    @Override
    @Transactional
    public User syncUser(Jwt jwt, Set<Role> roles) {
        String keycloakId = jwt.getSubject();
        log.info("Synchronizing user from JWT token with keycloakId: { {} }", keycloakId);

        return userRepository.findByKeycloakId(keycloakId)
                .map(user -> {
                    if (!user.getRoleSet().equals(roles)) {
                        log.info("Updating roles for user: {}", keycloakId);
                        user.getRoleSet().clear();
                        user.addToRoleSet(roles);
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseGet(() -> createNewUserWithProfileFromToken(jwt, roles, keycloakId));
    }

    private User createNewUserWithProfileFromToken(Jwt jwt, Set<Role> roles, String keycloakId) {
        String pictureUrl = jwt.getClaimAsString("picture");
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("title");

        User user = new User();
        user.setEmail(email);
        user.setKeycloakId(keycloakId);
        user.addToRoleSet(roles);

        Profile profile = new Profile();
        profile.setName(name);
        profile.setProfilePictureUrl(pictureUrl);
        profile.setPreferredWeightUnit(WeightUnit.KG);

        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
    }
}
