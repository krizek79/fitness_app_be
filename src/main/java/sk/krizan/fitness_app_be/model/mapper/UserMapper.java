package sk.krizan.fitness_app_be.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.controller.response.UserResponse;
import sk.krizan.fitness_app_be.model.entity.User;

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
}
