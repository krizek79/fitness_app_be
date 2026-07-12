package sk.krizan.fitness_app_be.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.reference.entity.DistanceUnit;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserRepository userRepository;

    /**
     * Runs in its own transaction so that a unique constraint violation caused by a concurrent
     * insert for the same keycloakId only rolls back this insert, not the caller's transaction.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User createNewUserWithProfileFromToken(Jwt jwt, String keycloakId) {
        String pictureUrl = jwt.getClaimAsString("picture");
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");

        User user = new User();
        user.setEmail(email);
        user.setKeycloakId(keycloakId);

        Profile profile = new Profile();
        profile.setName(name);
        profile.setProfilePictureUrl(pictureUrl);
        profile.setPreferredWeightUnit(WeightUnit.KG);
        profile.setPreferredDistanceUnit(DistanceUnit.KM);

        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.saveAndFlush(user);
    }

}
