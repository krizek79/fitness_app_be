package sk.krizan.fitness_app_be.controller.endpoint.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.response.UserResponse;
import sk.krizan.fitness_app_be.model.mapper.UserMapper;
import sk.krizan.fitness_app_be.service.api.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements sk.krizan.fitness_app_be.controller.endpoint.api.UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public UserResponse getUserById(@PathVariable Long id) {
        return UserMapper.entityToResponse(userService.getUserById(id));
    }
}

