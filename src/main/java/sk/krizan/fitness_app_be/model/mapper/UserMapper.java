package sk.krizan.fitness_app_be.model.mapper;

import java.time.Instant;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.request.SignUpRequest;
import sk.krizan.fitness_app_be.controller.response.UserResponse;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserResponse entityToResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .roles(user.getRoleSet())
            .profileResponse(ProfileMapper.entityToResponse(user.getProfile()))
            .build();
    }

    public static User signUpRequestToEntity(
        SignUpRequest request,
        Set<Role> roleSet,
        String encodedPassword
    ) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(encodedPassword);
        user.addToRoleSet(roleSet);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setActive(true);
        user.setEnabled(true);
        user.setLocked(false);
        user.setCredentialsNonExpired(true);

        return user;
    }
}
