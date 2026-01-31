package sk.krizan.fitness_app_be.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.domain.user.rest.dto.response.UserResponse;
import sk.krizan.fitness_app_be.domain.user.mapper.UserMapper;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements sk.krizan.fitness_app_be.domain.user.rest.api.UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public UserResponse getUserById(@PathVariable Long id) {
        return UserMapper.entityToResponse(userService.getUserById(id));
    }
}

