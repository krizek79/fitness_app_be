package sk.krizan.fitness_app_be.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.AuthenticationResponse;
import sk.krizan.fitness_app_be.controller.response.UserResponse;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationHelper {

    public static SignUpRequest createSignUpRequest(String email, String password, String matchingPassword) {
        return SignUpRequest.builder()
                .email(email)
                .password(password)
                .matchingPassword(matchingPassword)
                .build();
    }

    public static void assertSignedUpUserAndProfile(User user, SignUpRequest request) {
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(request.email(), user.getEmail());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertNotEquals(user.getPassword(), request.password());
        Assertions.assertTrue(user.getActive());
        Assertions.assertTrue(user.getEnabled());
        Assertions.assertTrue(user.getCredentialsNonExpired());
        Assertions.assertFalse(user.getLocked());
        Assertions.assertEquals(Set.of(Role.USER), user.getRoleSet());

        Profile profile = user.getProfile();
        Assertions.assertNotNull(profile);
        Assertions.assertNotNull(profile.getId());
        Assertions.assertNotNull(profile.getName());
        Assertions.assertEquals("", profile.getProfilePictureUrl());
        Assertions.assertFalse(profile.getDeleted());
        Assertions.assertEquals(WeightUnit.KG, profile.getPreferredWeightUnit());
        Assertions.assertEquals(List.of(), profile.getAuthoredCycleList());
        Assertions.assertEquals(List.of(), profile.getAssignedCycleList());
        Assertions.assertEquals(List.of(), profile.getAuthoredWorkoutList());
        Assertions.assertEquals(List.of(), profile.getAssignedWorkoutList());
        Assertions.assertEquals(Set.of(), profile.getCoachingSet());
        Assertions.assertEquals(Set.of(), profile.getBeingCoachedSet());
    }

    public static void assertSignInLocal_success(User user, AuthenticationResponse response) {
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.token());
        Assertions.assertNotNull(response.expiresAt());

        UserResponse userResponse = response.userResponse();
        UserHelper.assertUserResponse(user, userResponse);
    }
}
