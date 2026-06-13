package sk.krizan.fitness_app_be.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.user.mapper.UserMapper;
import sk.krizan.fitness_app_be.domain.user.rest.dto.response.UserResponse;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements sk.krizan.fitness_app_be.domain.user.rest.api.UserController {

    private final UserService userService;

    @Override
    public UserResponse getOrCreateCurrentUser() {
        return UserMapper.entityToResponse(userService.getOrCreateCurrentUser());
    }

}

