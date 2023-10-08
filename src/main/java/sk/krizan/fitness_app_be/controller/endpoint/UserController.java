package sk.krizan.fitness_app_be.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.krizan.fitness_app_be.controller.response.UserResponse;
import sk.krizan.fitness_app_be.model.mapper.UserMapper;
import sk.krizan.fitness_app_be.service.api.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse getUserById(@PathVariable Long id) {
        return UserMapper.entityToResponse(userService.getUserById(id));
    }
}

