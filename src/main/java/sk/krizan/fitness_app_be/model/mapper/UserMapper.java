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
            .roles(user.getRoles())
            .profileResponse(ProfileMapper.entityToResponse(user.getProfile()))
            .build();
    }

    public static User signUpRequestToEntity(
        SignUpRequest request,
        Set<Role> roles,
        String encodedPassword
    ) {
        return User.builder()
            .email(request.email())
            .password(encodedPassword)
            .roles(roles)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .active(true)
            .enabled(true)
            .locked(false)
            .credentialsNonExpired(true)
            .build();
    }
}
